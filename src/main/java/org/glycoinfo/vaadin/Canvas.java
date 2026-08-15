package org.glycoinfo.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;

/**
 * Base class of a canvas component that you can draw shapes and images on.
 * It is a Java wrapper for the <a href="https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API">HTML5 canvas</a>.
 * Use {@link #getContext()} to get the API for rendering shapes and images on the canvas.
 *
 * <p>Originally provided by the {@code org.vaadin.pekkam:canvas-java} add-on. It was absorbed into this
 * add-on so the only external dependency (which targeted an old Vaadin generation) could be removed.</p>
 */
@Tag("canvas")
public class Canvas extends Component implements HasStyle, HasSize {
    /**
     * Rendering context to draw shapes and images on the canvas.
     */
    private CanvasRenderingContext2D context;

    /**
     * Creates a canvas with the given size in pixels.
     * @param width Width of the canvas.
     * @param height Height of the canvas.
     */
    public Canvas(int width, int height) {
        context = new CanvasRenderingContext2D(this);
        getElement().setAttribute("width", String.valueOf(width));
        getElement().setAttribute("height", String.valueOf(height));
    }

    /**
     * Gets the context for rendering shapes and images on the canvas.
     * @return Returns the rendering context 2D of the canvas.
     */
    public CanvasRenderingContext2D getContext() {
        return context;
    }
}
