package org.vaadin.pwa;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;


@Tag("hello-world-element")
@HtmlImport("src/hello-world-element.html")
public class HelloWorldComponent extends PolymerTemplate<HelloWorldComponent.Model> {

    public interface Model extends TemplateModel {
        void setName(String name);
    }

    public void setName(String name) {
        getModel().setName(name);
    }
}
