package org.vaadin.pwa;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;


@Tag("webdsp-element")
@HtmlImport("src/webdsp-element.html")
public class WebDSP extends PolymerTemplate<WebDSP.WebDspModel> {

    public interface WebDspModel extends TemplateModel {
        void setEnableWebcam(Boolean enableWebcam);
        Boolean getEnableWebcam();
        void setFilter(String filter);
    }

    public enum Filter {
        NONE,
        INVERT,
        DEWDROPS,
        SOBEL
    }

    public void setWebcamEnabled(Boolean isEnabled) {
        getModel().setEnableWebcam(isEnabled);
    }

    public Boolean isWebcamEnabled() {
        return getModel().getEnableWebcam();
    }

    public void setFilter(Filter filter) {
        getModel().setFilter(filter.name());
    }
}
