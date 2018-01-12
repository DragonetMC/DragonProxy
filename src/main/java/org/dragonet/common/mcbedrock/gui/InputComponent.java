package org.dragonet.common.mcbedrock.gui;

import org.json.JSONObject;

/**
 * Created on 2017/12/26.
 */
public class InputComponent extends BaseModalFormComponent {

    private String text;

    private String defaultValue;

    private String placeholder;

    public InputComponent(String text) {
        super("input");
        this.text = text;
    }

    public InputComponent setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public InputComponent setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    @Override
    public void serializeData(JSONObject out) {
        out.put("text", text);
        if (defaultValue != null) {
            out.put("default", defaultValue);
        }
        if (placeholder != null) {
            out.put("placeholder", placeholder);
        }
    }
}
