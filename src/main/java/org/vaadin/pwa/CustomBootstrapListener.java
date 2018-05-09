package org.vaadin.pwa;

import org.jsoup.nodes.Element;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;

/**
 * Modifies the Vaadin bootstrap page (the HTTP response) in order to
 * <ul>
 * <li>add service worker</li>
 * <li>add a link to the web app manifest</li>
 * <li>add links to favicons</li>
 * </ul>
 */
public class CustomBootstrapListener implements BootstrapListener {
    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {

        final Element head = response.getDocument().head();

        // manifest needs to be prepended before scripts or it won't be loaded
        head.prepend("<meta name='theme-color' content='#227aef'>");
        head.prepend("<link rel='manifest' href='manifest.json'>");

        // Add service worker
        response.getDocument().body().appendElement("script")
                .attr("src", "register-sw.js");

        // add additional meta tags
        head.append("<meta name='viewport' content='width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes'>");
        head.append("<link rel='shortcut icon' href='favicon.ico'>");
        head.append("<link rel='icon' sizes='192x192' href='frontend/images/icons/icon-192x192.png'>");
        head.append("<link rel='icon' sizes='96x96' href='frontend/images/icons/icon-96x96.png'>");
        head.append("<link rel='apple-touch-icon' sizes='192x192' href='frontend/images/icons/icon-192x192.png'>");
        head.append("<link rel='apple-touch-icon' sizes='96x96' href='frontend/images/icons/icon-96x96.png'>");
    }

}