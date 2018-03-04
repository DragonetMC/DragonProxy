package org.dragonet.common.gui;

import com.google.gson.JsonObject;

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
    public void serializeData(JsonObject out) {
        out.addProperty("text", text);
        if (defaultValue != null) {
            out.addProperty("default", defaultValue);
        }
        if (placeholder != null) {
            out.addProperty("placeholder", placeholder);
        }
    }
}
