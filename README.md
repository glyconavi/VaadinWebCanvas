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
    <version>1.0.0.11</version>
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
    StreamResource streamResource = new StreamResource("sample.png", () -> new ByteArrayInputStream(imageBytes));
    Anchor downloadImageAnchor = new Anchor(VaadinSession.getCurrent().getResourceRegistry().registerResource(streamResource).getResource(), "Download Image");
    downloadImageAnchor.getElement().setAttribute("download", "structures.png");
    add(downloadImageAnchor);
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
`MainView` and `AppShell` are the demo the example above is taken from. They are kept as the
worked example, but they are no longer compiled or run by the build: `MainView` registers its
download link through `VaadinSession.getResourceRegistry()`, which Vaadin 25 removed, and the
demo also needs a servlet container newer than the Jetty 9 it used to run on. The
`mvn jetty:run -P debug` of earlier versions - and the "Maven Run" task in `.vscode/tasks.json`
that calls it - therefore no longer work. Bringing the demo up to Vaadin 25 is worth doing and
has not been done.
