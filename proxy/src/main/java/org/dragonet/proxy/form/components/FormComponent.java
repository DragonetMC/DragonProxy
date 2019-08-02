package org.dragonet.proxy.form.components;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FormComponent {
    private String type;

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("type", type);
        return object;
    }
}
