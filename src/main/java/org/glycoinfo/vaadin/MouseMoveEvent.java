package org.glycoinfo.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * 
 *
 * @param <C>
 */
@DomEvent("mousemove")
@SuppressWarnings("serial")
public class MouseMoveEvent<C extends Component> extends ComponentEvent<C> {
    /**
     * 
     */
    private final double screenX;

    /**
     * 
     */
    private final double screenY;

    /**
     * 
     */
    private final double clientX;

    /**
     * 
     */
    private final double clientY;

    /**
     * 
     */
    private final double offsetX;

    /**
     * 
     */
    private final double offsetY;

    /**
     * 
     */
    private final int clickCount;

    /**
     * 
     */
    private final int button;

    /**
     * 
     */
    private final boolean ctrlKey;

    /**
     * 
     */
    private final boolean shiftKey;

    /**
     * 
     */
    private final boolean altKey;

    /**
     * 
     */
    private final boolean metaKey;

    /**
     * 
     * @param source
     * @param fromClient
     * @param screenX
     * @param screenY
     * @param clientX
     * @param clientY
     * @param offsetX
     * @param offsetY
     * @param clickCount
     * @param button
     * @param ctrlKey
     * @param shiftKey
     * @param altKey
     * @param metaKey
     */
    @SuppressWarnings("unchecked")
    public MouseMoveEvent(Component source, boolean fromClient,
            @EventData("event.screenX") double screenX,
            @EventData("event.screenY") double screenY,
            @EventData("event.clientX") double clientX,
            @EventData("event.clientY") double clientY,
            @EventData("event.offsetX") double offsetX,
            @EventData("event.offsetY") double offsetY,
            @EventData("event.detail") int clickCount,
            @EventData("event.button") int button,
            @EventData("event.ctrlKey") boolean ctrlKey,
            @EventData("event.shiftKey") boolean shiftKey,
            @EventData("event.altKey") boolean altKey,
            @EventData("event.metaKey") boolean metaKey) {
        super((C) source, fromClient);
        this.screenX = screenX;
        this.screenY = screenY;
        this.clientX = clientX;
        this.clientY = clientY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.clickCount = clickCount;
        this.button = button;
        this.ctrlKey = ctrlKey;
        this.shiftKey = shiftKey;
        this.altKey = altKey;
        this.metaKey = metaKey;
    }

    /**
     * 
     * @param source
     */
    public MouseMoveEvent(Component source) {
        this(source, false, -1, -1, -1, -1, 1, -1, -1, -1, false, false, false, false);
    }

    /**
     * Gets the x coordinate of the click event, relative to the upper left
     * corner of the screen.
     *
     * @return the x coordinate, -1 if unknown
     */
    public double getScreenX() {
        return screenX;
    }

    /**
     * Gets the y coordinate of the click event, relative to the upper left
     * corner of the screen.
     *
     * @return the y coordinate, -1 if unknown
     */
    public double getScreenY() {
        return screenY;
    }

    /**
     * Gets the x coordinate of the click event, relative to the upper left
     * corner of the browser viewport.
     *
     * @return the x coordinate, -1 if unknown
     */
    public double getClientX() {
        return clientX;
    }

    /**
     * Gets the y coordinate of the click event, relative to the upper left
     * corner of the browser viewport.
     *
     * @return the y coordinate, -1 if unknown
     */
    public double getClientY() {
        return clientY;
    }

    /**
     * 
     * @return
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * 
     * @return
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Gets the number of consecutive clicks recently recorded.
     *
     * @return the click count
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * Gets the id of the pressed mouse button.
     * <ul>
     * <li>-1: No button
     * <li>0: The primary button, typically the left mouse button
     * <li>1: The middle button,
     * <li>2: The secondary button, typically the right mouse button
     * <li>3: The first additional button, typically the back button
     * <li>4: The second additional button, typically the forward button
     * <li>5+: More additional buttons without any typical meanings
     * </ul>
     *
     * @return the button id, or -1 if no button was pressed
     */
    public int getButton() {
        return button;
    }

    /**
     * Checks whether the ctrl key was was down when the event was fired.
     *
     * @return <code>true</code> if the ctrl key was down when the event was
     *         fired, <code>false</code> otherwise
     */
    public boolean isCtrlKey() {
        return ctrlKey;
    }

    /**
     * Checks whether the alt key was was down when the event was fired.
     *
     * @return <code>true</code> if the alt key was down when the event was
     *         fired, <code>false</code> otherwise
     */
    public boolean isAltKey() {
        return altKey;
    }

    /**
     * Checks whether the meta key was was down when the event was fired.
     *
     * @return <code>true</code> if the meta key was down when the event was
     *         fired, <code>false</code> otherwise
     */
    public boolean isMetaKey() {
        return metaKey;
    }

    /**
     * Checks whether the shift key was was down when the event was fired.
     *
     * @return <code>true</code> if the shift key was down when the event was
     *         fired, <code>false</code> otherwise
     */
    public boolean isShiftKey() {
        return shiftKey;
    }
}
