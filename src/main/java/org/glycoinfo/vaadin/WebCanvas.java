package org.glycoinfo.vaadin;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.function.Consumer;

import org.vaadin.pekkam.Canvas;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

/**
 * Extended class of base class of canvas component that you can draw shapes and images on.
 * It's a Java wrapper for the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API">HTML5 canvas</a>.
 * Use {@link #getContext()} to get API for rendering shapes and images on the canvas.
 */
@SuppressWarnings("serial")
public class WebCanvas extends Canvas implements MouseDownNotifier<WebCanvas>, MouseMoveNotifier<WebCanvas>, MouseUpNotifier<WebCanvas>, KeyDownNotifier<WebCanvas>, SelectionListener {
    /**
     * Width of the canvas.
     */
    protected int width = 0;

    /**
     * Height of the canvas.
     */
    protected int height = 0;

    /**
     * Renderer to draw the shapes and images in the canvas.
     */
    protected WebCanvasRenderingContext2D renderer = new WebCanvasRenderingContext2D(this);

    /**
     * X coordinate on mouse down event.
     */
    private double mouseDownX = 0;

    /**
     * Y coordinate on mouse down event.
     */
    private double mouseDownY = 0;

    /**
     * Registration of mouse move listener.
     */
    private Registration mouseMoveListener = null;

    /**
     * Calls super class constructor , registers listeners.
     * @param width Width of the canvas.
     * @param height Height of the canvas.
     */
    public WebCanvas(int width, int height) {
        super(width, height);

        // Registers listeners.
        addMouseDownListener();
        addMouseUpListener();
        addKeyDownListener();
    }

    /**
     * Sets width and height of the canvas.
     * @param width Changing width.
     * @param height Changing height.
     */
    public void setSize(int width, int height) {
        getElement().setAttribute("width", String.valueOf(width));
        getElement().setAttribute("height", String.valueOf(height));
    }

    /**
     * Getse canvas width.
     * @return Returns canvas width.
     */
    public int getCanvasWidth() {
        int width = 0;
        try {
            width = Integer.parseInt(getElement().getAttribute("width"));
        } catch (Exception e) {
        }
        return width;
    }

    /**
     * Gets canvas height.
     * @return Returns canvas height.
     */
    public int getCanvasHeight() {
        int height = 0;
        try {
            height = Integer.parseInt(getElement().getAttribute("height"));
        } catch (Exception e) {
        }
        return height;
    }

    /**
     * Adds mouse down listener.
     */
    private void addMouseDownListener() {
        addMouseDownListener(mouseDownEvent -> {
            // Retains x and y coordinate and receives that updates selection on the canvas.
            mouseDownX = mouseDownEvent.getOffsetX();
            mouseDownY = mouseDownEvent.getOffsetY();
            receiveUpdatingSelection(mouseDownX, mouseDownY, 0, 0, false);

            // Adds mouse move listener.
            if (mouseMoveListener == null) mouseMoveListener = addMouseMoveListener();
        });
    }

    /**
     * Adds mouse move listener.
     */
    private Registration addMouseMoveListener() {
        return addMouseMoveListener(mouseMoveEvent -> {
            // Gets rectangle selection renderer.
            WebCanvasRenderingContext2D rectangleSelectionRenderer = getRectangleSelectionRenderer();

            // Draws a rectangle selection on the canvas using x and y coordinate on mouse down and move event.
            double mouseMoveX = mouseMoveEvent.getOffsetX();
            double rectangleSelectionX = mouseDownX < mouseMoveX ? mouseDownX : mouseMoveX;
            double rectangleSelectionWidth = Math.abs(mouseDownX - mouseMoveX);
            double mouseMoveY = mouseMoveEvent.getOffsetY();
            double rectangleSelectionY = mouseDownY < mouseMoveY ? mouseDownY : mouseMoveY;
            double rectangleSelectionHeight = Math.abs(mouseDownY - mouseMoveY);
            rectangleSelectionRenderer.save();
            rectangleSelectionRenderer.setLineWidth(1d);
            rectangleSelectionRenderer.setStrokeStyle("#000");
            rectangleSelectionRenderer.strokeRect(rectangleSelectionX, rectangleSelectionY, rectangleSelectionWidth, rectangleSelectionHeight);
            rectangleSelectionRenderer.restore();
        });
    }

    /**
     * Adds mouse up listener.
     */
    private void addMouseUpListener() {
        addMouseUpListener(mouseUpEvent -> {
            // Clears rectangle selection.
            clearRectangleSelection();

            // Removes mouse move listener.
            mouseMoveListener.remove();
            mouseMoveListener = null;

            // If mouse donesn't move during mouse down and up event, doesn't execute the process.
            double mouseUpX = mouseUpEvent.getOffsetX();
            double mouseUpY = mouseUpEvent.getOffsetY();
            if (mouseDownX == mouseUpX && mouseDownY == mouseUpY) return;

            // Executes updating selection.
            double rectangleSelectionX = mouseDownX < mouseUpX ? mouseDownX : mouseUpX;
            double rectangleSelectionWidth = Math.abs(mouseDownX - mouseUpX);
            double rectangleSelectionY = mouseDownY < mouseUpY ? mouseDownY : mouseUpY;
            double rectangleSelectionHeight = Math.abs(mouseDownY - mouseUpY);
            receiveUpdatingSelection(rectangleSelectionX, rectangleSelectionY, rectangleSelectionWidth, rectangleSelectionHeight, true);
        });
    }

    /**
     * Adds key down listener.
     */
    private void addKeyDownListener() {
        getElement().setAttribute("tabindex", "0");
        addKeyDownListener(keyDownEvent -> {
            if (keyDownEvent.isCtrlKey() && keyDownEvent.getKey().equals("c")) {
                copyImageToClipboard();
            }
        });
    }

    /**
     * Receives that updates selection on the canvas.
     */
    @Override
    public void receiveUpdatingSelection(double x, double y, double width, double height, boolean isMoved) {
        updateSelection(x, y, width, height, isMoved);
    }

    /**
     * Updates selection on the canvas.
     * @param x X coordinate with mouse pointer.
     * @param y Y coordinate with mouse pointer.
     * @param width Width of rectangle selection.
     * @param height Height of rectangle selection.
     * @param isMoved Whether mouse is moved.
     */
    protected void updateSelection(double x, double y, double width, double height, boolean isMoved) {
    }

    /**
     * Gets rectangle selection renderer.
     * @return Returns rectangle selection renderer.
     */
    private WebCanvasRenderingContext2D getRectangleSelectionRenderer() {
        // Clears the canvas and redraws shapes on the canvas for erasing previous rectangle selection.
        // Uses other WebCanvasRenderingContext2D instance for not drawing a rectangle selection continuously.
        // The WebCanvasRenderingContext2D.redraw method executes using history of executed drawing processes.
        WebCanvasRenderingContext2D rectangleSelectionRenderer = new WebCanvasRenderingContext2D(this);
        String width = getElement().getAttribute("width");
        String height = getElement().getAttribute("height");
        rectangleSelectionRenderer.clear(0, 0, Double.parseDouble(width), Double.parseDouble(height));
        renderer.redraw();
        return rectangleSelectionRenderer;
    }

    /**
     * Clears rectangle selection.
     */
    private void clearRectangleSelection() {
        getRectangleSelectionRenderer();
    }

    /**
     * Copies image from canvas to clipboard.
     */
    public void copyImageToClipboard() {
        Element webCanvasElement = getElement();
        webCanvasElement.executeJs(
            "this.toBlob(function (blob) {" +
            "   const item = new ClipboardItem({'image/png': blob});" +
            "   navigator.clipboard.write([item]);" +
            "}, 'image/png');");
    }

    /**
     * Converts this canvas to an image.
     * @param resultHandler Result handler for handling imaged canvas.
     */
    public void toImage(Consumer<Image> resultHandler) {
        this.toDataURL(dataURI -> {
            String dataURIBase64 = dataURI.replace("data:image/png;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(dataURIBase64.getBytes());
            StreamResource streamResource = new StreamResource("", () -> new ByteArrayInputStream(imageBytes));
            Image image = new Image(streamResource, "");
            resultHandler.accept(image);
        });
    }

    /**
     * Converts this canvas to data url.
     * @param resultHandler Handler after converting this canvas to data url.
     */
    public void toDataURL(Consumer<String> resultHandler) {
        toDataURL("image/png", resultHandler);
    }

    /**
     * Converts this canvas to data url.
     * @param type Type of image.
     * @param resultHandler resultHandler Handler after converting this canvas to data url.
     */
    public void toDataURL(String type, Consumer<String> resultHandler) {
        toDataURL(type, 0.92d, resultHandler);
    }

    /**
     * Converts this canvas to data url.
     * @param type Type of image.
     * @param encoderOptions Image quality for image formats that use lossy compression.
     * @param resultHandler resultHandler Handler after converting this canvas to data url.
     */
    public void toDataURL(String type, double encoderOptions, Consumer<String> resultHandler) {
        if (type == null) type = "image/png";
        PendingJavaScriptResult pendingJavaScriptResult = callJsMethod("toDataURL", type, encoderOptions);
        pendingJavaScriptResult.then(String.class, dataURI -> {
            resultHandler.accept(dataURI);
        });
    }

    /**
     * Calls JavaScript method.
     * @param methodName Method name that runs.
     * @param parameters Parameters that passes the method.
     * @return Returns the result of JavaScript method.
     */
    protected PendingJavaScriptResult callJsMethod(String methodName, Serializable... parameters) {
        return getElement().callJsFunction(methodName, parameters);
    }

    /**
     * Gets the context for rendering shapes and images in the canvas.
     * It is a Java wrapper for the <a href="https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D">same client-side API</a>.
     * 
     * @return Returns the rendering context 2D of the canvas.
     */
    @Override
    public WebCanvasRenderingContext2D getContext() {
        return renderer;
    }
}
