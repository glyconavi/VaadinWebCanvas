package org.glycoinfo.vaadin.demo;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import org.glycoinfo.vaadin.WebCanvas;
import org.glycoinfo.vaadin.WebCanvasRenderingContext2D;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

/**
 * The main view for web canvas.
 */
@Route
@SuppressWarnings("serial")
public class MainView extends VerticalLayout {
    /**
     * Creates main view for web canvas.
     */
    public MainView() {
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
    }
}
