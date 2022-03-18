package org.glycoinfo.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

/**
 * This class is used to configure the generated html host page used by the app
 */
@PWA(name = "My Application", shortName = "My Application")
public class AppShell implements AppShellConfigurator {
    
}