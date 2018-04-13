package org.vaadin.pwa;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.DependencyFilter;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

/**
 * Configures the {@link VaadinService}:
 * <ul>
 *   <li>adds a {@link BootstrapListener} to add favicons, viewport,
 *   etc to the initial HTML sent to the browser (see {@link CustomBootstrapListener})</li>
 *   <li>adds a {@link DependencyFilter} to allow dependency bundling
 *   in the production mode (when all individual .html dependencies are combined a single
 *   file to improve the page load performance)</li>
 * </ul>
 */
public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(new CustomBootstrapListener());
    }
}