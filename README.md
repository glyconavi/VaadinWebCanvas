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

The build produces the library jar, which is what `mvn deploy` publishes to
[glycoinfo/MavenRepository](https://github.com/glycoinfo/MavenRepository). Built on JDK 25 it
reproduces the published 1.0.0.11 artifact byte for byte.

### The demo
The example above is `demo/`, a small application that runs the add-on the way a consumer does:

```bash
mvn install          # only needed to try a local change to the add-on
cd demo && mvn jetty:run   # then http://localhost:8080
```

It is deliberately not a module of the library build - it depends on the published add-on, so the
library pom stays a standalone pom, which is what is published and what consumers read. It needs
Node as well as JDK 21+; Vaadin fetches the frontend toolchain on the first run.
