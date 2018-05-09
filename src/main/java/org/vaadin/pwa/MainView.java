package org.vaadin.pwa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
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
@PageTitle("Vaadin Flow PWA Example")
public class MainView extends VerticalLayout {

    public MainView() {

        final HelloWorldComponent helloWorld = new HelloWorldComponent();

        final TextField nameInput = new TextField("Name");

        Button submitBtn = new Button("Submit");
        submitBtn.addClickListener(e -> helloWorld.setName(nameInput.getValue()));

        add(nameInput);
        add(submitBtn);
        add(helloWorld);

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
