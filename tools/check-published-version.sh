#!/usr/bin/env bash
# Fails when the version in the pom is already published and this tree builds something different.
#
# "A published version is never rebuilt in place" is a rule the README states and nothing enforced.
# Consumers resolve by coordinates and cache what they get, so two different jars under one version
# is two different libraries with one name. This is the check that says so before a merge, not after
# somebody notices their build changed under them.
set -euo pipefail

REPO_URL=https://raw.githubusercontent.com/glycoinfo/MavenRepository/master
GROUP_PATH=org/glycoinfo/vaadin
ARTIFACT=vaadin-web-canvas

version=$(mvn -B -q help:evaluate -Dexpression=project.version -DforceStdout)
built=target/${ARTIFACT}-${version}.jar
[ -f "$built" ] || { echo "no built jar at $built - run mvn package first"; exit 1; }

echo "pom version: $version"

metadata=$(curl -fsSL "${REPO_URL}/${GROUP_PATH}/${ARTIFACT}/maven-metadata.xml" || true)
# Reported for a caller that cares which of the three answers this was - release.yml asks a
# person to publish only when there is something to publish.
report() { [ -n "${GITHUB_OUTPUT:-}" ] && echo "state=$1" >> "$GITHUB_OUTPUT"; return 0; }

if ! grep -q "<version>${version}</version>" <<<"$metadata"; then
    echo "not published yet - nothing to compare against, and nothing to protect"
    report unpublished
    exit 0
fi

echo "already published - comparing what this tree builds against it"
work=$(mktemp -d)
curl -fsSL "${REPO_URL}/${GROUP_PATH}/${ARTIFACT}/${version}/${ARTIFACT}-${version}.jar" -o "$work/published.jar"

# Class files only. The manifest carries the builder's name and JDK, the embedded pom.properties
# carries a timestamp, and the bundled .java sources differ by line endings between the machine that
# first built a version and this one - none of which is the library behaving differently.
mkdir -p "$work/pub" "$work/new"
(cd "$work/pub" && unzip -qo "$work/published.jar" '*.class')
(cd "$work/new" && unzip -qo "$OLDPWD/$built" '*.class')

if diff -r "$work/pub" "$work/new" >"$work/diff.txt" 2>&1; then
    echo "identical: $(find "$work/pub" -name '*.class' | wc -l | tr -d ' ') class files match the published $version"
    report identical
    exit 0
fi

cat <<MESSAGE

$version is published, and this tree builds different bytecode:

$(sed 's/^/  /' "$work/diff.txt")

Bump the version. Replacing a published jar gives two different artifacts the same
coordinates, and everyone who already resolved it keeps the old one.
MESSAGE
report differs
exit 1
