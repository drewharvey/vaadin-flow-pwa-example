package org.vaadin.pwa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view contains a button and a template element.
 */
@BodySize(height = "100vh", width = "100vw")
@HtmlImport("styles/shared-styles.html")
@Route("")
@Theme(Lumo.class)
public class MainView extends VerticalLayout {

    public MainView() {

        // create layout

        WebDSP dsp = new WebDSP();
        add(dsp);

        HorizontalLayout buttons = new HorizontalLayout();
        add(buttons);

        Button toggleWebcam = new Button("Toggle Webcam");
        toggleWebcam.addClickListener(e -> dsp.setWebcamEnabled(!dsp.isWebcamEnabled()));
        buttons.add(toggleWebcam);

        Button originalBtn = new Button("Original");
        originalBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.NONE));
        buttons.add(originalBtn);

        Button invertBtn = new Button("Invert");
        invertBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.INVERT));
        buttons.add(invertBtn);

        Button dewdropBtn = new Button("Dewdrops");
        dewdropBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.DEWDROPS));
        buttons.add(dewdropBtn);

        Button sobelBtn = new Button("Sobel");
        sobelBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.SOBEL));
        buttons.add(sobelBtn);

        // start webcam
        dsp.setWebcamEnabled(true);
    }

    // TODO: use PageConfigurator interface instead of BootstrapListener
//    @Override
//    public void configurePage(InitialPageSettings settings) {
//        settings.addLink("manifest", "manifest.json");
//        // TODO: add js file?
//        // settings.addInlineFromFile("frontend/src/register-sw.js", InitialPageSettings.WrapMode.JAVASCRIPT);
//        settings.addInlineWithContents("if ('serviceWorker' in navigator) { " +
//                "    navigator.serviceWorker.register('sw.js').then(function(registration) { " +
//                "        console.log('Service worker registration succeeded:', registration); " +
//                "    }).catch(function(error) { " +
//                "        console.log('Service worker registration failed:', error); " +
//                "    });\n" +
//                "}", InitialPageSettings.WrapMode.JAVASCRIPT);
//    }
}
