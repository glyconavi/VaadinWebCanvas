package org.glycoinfo.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * 
 * @author glyco
 *
 * @param <C>
 */
@DomEvent("keyup")
@SuppressWarnings("serial")
public class KeyDownEvent<C extends Component> extends ComponentEvent<C> {

    /**
     * 
     */
    private final String key;

    /**
     * 
     */
    private final String code;

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
     * @param key
     * @param code
     * @param ctrlKey
     * @param shiftKey
     * @param altKey
     * @param metaKey
     */
    @SuppressWarnings("unchecked")
    public KeyDownEvent(Component source, boolean fromClient,
            @EventData("event.key") String key,
            @EventData("event.code") String code,
            @EventData("event.ctrlKey") boolean ctrlKey,
            @EventData("event.shiftKey") boolean shiftKey,
            @EventData("event.altKey") boolean altKey,
            @EventData("event.metaKey") boolean metaKey) {
        super((C) source, fromClient);
        this.key = key;
        this.code = code;
        this.ctrlKey = ctrlKey;
        this.shiftKey = shiftKey;
        this.altKey = altKey;
        this.metaKey = metaKey;
    }

    /**
     * 
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @return
     */
    public boolean isCtrlKey() {
        return ctrlKey;
    }

    /**
     * 
     * @return
     */
    public boolean isShiftKey() {
        return shiftKey;
    }

    /**
     * 
     * @return
     */
    public boolean isAltKey() {
        return altKey;
    }

    /**
     * 
     * @return
     */
    public boolean isMetaKey() {
        return metaKey;
    }
}
