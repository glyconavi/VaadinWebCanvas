# VaadinWebCanvas
This is a Canvas component for Vaadin. This component provides a some function of [the client side JavaScriptAPI](https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API) to Java users.

Up to 1.0.0.9 the component was built for Vaadin 14+ and drew its base classes from
[org.vaadin.pekkam:canvas-java](https://vaadin.com/directory/component/canvas). That add-on was
never rebuilt past Vaadin 14, so from 1.0.0.10 the two classes it supplied - `Canvas` and
`CanvasRenderingContext2D` - are part of this project, and the component is built against
Vaadin 25. Nothing else about the component changed: the API is the same.

### How to use
Add the repository and dependency to your Vaadin project's pom.xml:
```xml
<repository>
    <id>github</id>
    <name>my github repository</name>
    <url>https://raw.githubusercontent.com/glycoinfo/MavenRepository/master/</url>
</repository>
```
```xml
<dependency>
    <groupId>org.glycoinfo.vaadin</groupId>
    <artifactId>vaadin-web-canvas</artifactId>
    <version>1.0.0.12</version>
</dependency>
```

You can use the cliant side JavaScriptAPI below:
```java
// Creates web canvas, and draws rectangle.
WebCanvas webCanvas = new WebCanvas(800, 800);
WebCanvasRenderingContext2D renderer = webCanvas.getContext();
renderer.setFillStyle("#111");
renderer.fillRect(10, 10, 100, 80);
add(webCanvas);

// Outputs image of be drawn canvas when clicking the anchor.
webCanvas.toDataURL("image/png", dataURI -> {
    // Calls image dialog to display image.
    String dataURIBase64 = dataURI.replace("data:image/png;base64,", "");
    byte[] imageBytes = Base64.getDecoder().decode(dataURIBase64.getBytes());
    DownloadHandler downloadImage = DownloadHandler.fromInputStream(event -> new DownloadResponse(
            new ByteArrayInputStream(imageBytes), "structures.png", "image/png", imageBytes.length));
    add(new Anchor(downloadImage, "Download Image"));
});

// Adds the image to MainView.
webCanvas.toImage(image -> {
    add(image);
});
```

### How to build
* Clone this repository
* `mvn package`, with JDK 21 or later

The build produces the library jar. When the source came home at 1.0.0.11 the tree built, on JDK 25,
byte for byte identical to the artifact already published under that version — which is how we know
this is the source it was built from. That is a statement about 1.0.0.11, not a property to expect
from every later version: 1.0.0.12 changes `toImage`, so it is a different jar on purpose.

### How to publish
Releases go to
[glycoinfo/MavenRepository](https://github.com/glycoinfo/MavenRepository/tree/master/org/glycoinfo/vaadin/vaadin-web-canvas),
which is an ordinary git repository served raw over HTTPS: publishing is a commit to it, so it can
be reviewed and reverted like any other change. Consumers point at
`https://raw.githubusercontent.com/glycoinfo/MavenRepository/master`.

1. Set the new version in `pom.xml` (and in `demo/pom.xml`, both its own version and the dependency),
   merge that to `main` the way the project's other releases were merged, and tag it.
2. Clone the repository you are publishing to, and deploy into your clone:
   ```bash
   git clone https://github.com/glycoinfo/MavenRepository.git
   mvn clean deploy -DaltDeploymentRepository=publish::default::file:///absolute/path/to/MavenRepository
   ```
   **Deploy into the clone, not into `target/mvn-repo`.** Maven merges `maven-metadata.xml` against
   whatever it finds at the destination: into the clone, the new version is appended and `<release>`
   moves forward; into an empty directory, the metadata it writes lists only the version just built,
   and copying that over the repository's own would drop every earlier release from it. That is the
   trap that retired the old `site-maven-plugin` step, which pushed `target/mvn-repo` straight onto
   `master`.
3. Check what appeared — the version directory should hold the jar, the pom and their `.md5`/`.sha1`,
   and `maven-metadata.xml` should still list every earlier version — then commit and open a pull
   request. 1.0.0.10 and 1.0.0.11 went in this way, as
   [MavenRepository#2](https://github.com/glycoinfo/MavenRepository/pull/2).
4. Verify from the outside, not from your own machine's cache:
   ```bash
   rm -rf ~/.m2/repository/org/glycoinfo/vaadin
   mvn -U dependency:get -Dartifact=org.glycoinfo.vaadin:vaadin-web-canvas:<new version>
   ```

`mvn deploy` on its own stages into `target/mvn-repo` and publishes nothing, so it is safe to run.

**A published version is never rebuilt in place.** Consumers resolve it by coordinates and cache it;
replacing a jar under a version that is already out gives two different artifacts the same name. If
something needs changing, it needs a new version.

### The demo
The example above is `demo/`, a small application that runs the add-on the way a consumer does:

```bash
mvn install          # only needed to try a local change to the add-on
cd demo && mvn jetty:run   # then http://localhost:8080
```

It is deliberately not a module of the library build - it depends on the published add-on, so the
library pom stays a standalone pom, which is what is published and what consumers read. It needs
Node as well as JDK 21+; Vaadin fetches the frontend toolchain on the first run.
