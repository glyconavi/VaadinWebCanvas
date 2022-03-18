package org.glycoinfo.vaadin;

import java.io.Serializable;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.shared.Registration;

/**
 * 
 *
 * @param <T>
 */
public interface MouseDownNotifier<T extends Component> extends Serializable {
    /**
     * 
     * @param listener
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    default Registration addMouseDownListener(ComponentEventListener<MouseDownEvent<T>> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this, MouseDownEvent.class, (ComponentEventListener) listener);
        } else {
            throw new IllegalStateException(String.format("The class '%s' doesn't extend '%s'. " + "Make your implementation for the method '%s'.", getClass().getName(), Component.class.getSimpleName(), "addMouseDownListener"));
        }
    }
}
