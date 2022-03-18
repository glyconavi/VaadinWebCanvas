package org.glycoinfo.vaadin;

/**
 * 
 */
public interface SelectionListener {
    /**
     * Receives that updates selection on the canvas.
     * @param x X coordinate with mouse pointer.
     * @param y Y coordinate with mouse pointer.
     * @param width Width of selection rectangle.
     * @param height Height of selection rectangle.
     * @param isMoved Whether mouse is moved.
     */
    public void receiveUpdatingSelection(double x, double y, double width, double height, boolean isMoved);
}
