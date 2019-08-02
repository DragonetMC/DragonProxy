package org.dragonet.proxy.form.components;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class LabelComponent extends FormComponent {
    private String text;

    public LabelComponent(String text) {
        super("label");
        this.text = text;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = super.serialize();
        object.addProperty("text", text);
        return object;
    }
}
