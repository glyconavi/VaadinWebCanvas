# VaadinWebCanvas
This is a Canvas component for Vaadin 14+. This component provides a some function of [the client side JavaScriptAPI](https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API) to Java users.

### How to use
Add the repository and dependency to your Vaadin 14+ project's pom.xml:
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
    <version>1.0.0.5</version>
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

### How to test
* Clone this repository
* `jetty:run -P debug`
* This deploys the demo at `http://localhost:8080`