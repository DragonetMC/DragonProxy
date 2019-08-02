package org.dragonet.proxy.form.components;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class InputComponent extends FormComponent {
    private String text;
    private String defaultValue;
    private String placeholder;

    public InputComponent(String text, String placeholder) {
        this(text, placeholder, null);
    }

    public InputComponent(String text, String placeholder, String defaultValue) {
        super("input");
        this.text = text;
        this.placeholder = placeholder;
        this.defaultValue = defaultValue;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = super.serialize();
        object.addProperty("text", text);
        if(defaultValue != null) {
            object.addProperty("default", defaultValue);
        }
        if(placeholder != null) {
            object.addProperty("placeholder", placeholder);
        }
        return object;
    }
}
