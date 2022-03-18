package org.glycoinfo.vaadin;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

/**
 * The main view contains a button and a click listener.
 */
@Route
@SuppressWarnings("serial")
public class MainView extends VerticalLayout {
    /**
     * 
     */
    public MainView() {
        //
        WebCanvas webCanvas = new WebCanvas(800, 800);
        WebCanvasRenderingContext2D renderer = webCanvas.getContext();
        renderer.setFillStyle("#111");
        renderer.fillRect(10, 10, 100, 80);
        add(webCanvas);

        //
        webCanvas.toDataURL("image/png", dataURI -> {
            // Calls image dialog to display image.
            String dataURIBase64 = dataURI.replace("data:image/png;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(dataURIBase64.getBytes());
            StreamResource streamResource = new StreamResource("sample.png", () -> new ByteArrayInputStream(imageBytes));
            Anchor downloadImageAnchor = new Anchor(VaadinSession.getCurrent().getResourceRegistry().registerResource(streamResource).getResource(), "Download Image");
            downloadImageAnchor.getElement().setAttribute("download", "structures.png");
            add(downloadImageAnchor);
        });
    }
}
