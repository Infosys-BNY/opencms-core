package org.opencms.content.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class WidgetConfigurationService {
    
    private static final Map<String, String> WIDGET_MAPPINGS = new HashMap<>();
    
    static {
        WIDGET_MAPPINGS.put("string", "org.opencms.widgets.CmsInputWidget");
        WIDGET_MAPPINGS.put("select", "org.opencms.widgets.CmsSelectWidget");
        WIDGET_MAPPINGS.put("multicheck", "org.opencms.widgets.CmsMultiSelectWidget");
        WIDGET_MAPPINGS.put("selectcombo", "org.opencms.widgets.CmsSelectComboWidget");
        WIDGET_MAPPINGS.put("checkbox", "org.opencms.widgets.CmsCheckboxWidget");
        WIDGET_MAPPINGS.put("combo", "org.opencms.widgets.CmsComboWidget");
        WIDGET_MAPPINGS.put("datebox", "org.opencms.widgets.CmsCalendarWidget");
        WIDGET_MAPPINGS.put("gallery", "org.opencms.widgets.CmsVfsFileWidget");
        WIDGET_MAPPINGS.put("multiselectbox", "org.opencms.widgets.CmsMultiSelectWidget");
        WIDGET_MAPPINGS.put("radio", "org.opencms.widgets.CmsRadioSelectWidget");
        WIDGET_MAPPINGS.put("groupselection", "org.opencms.widgets.CmsGroupWidget");
    }
    
    public String getWidgetClass(String widgetName) {
        return WIDGET_MAPPINGS.get(widgetName);
    }
    
    public Map<String, String> getAllWidgetMappings() {
        return new HashMap<>(WIDGET_MAPPINGS);
    }
}
