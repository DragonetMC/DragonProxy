package org.dragonet.proxy.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.dragonet.proxy.form.components.FormComponent;

import java.util.ArrayList;
import java.util.List;

public class CustomForm extends Form {
    @Getter
    private final List<FormComponent> components = new ArrayList<>();

    public CustomForm(String title) {
        super("custom_form", title);
    }

    // TODO: @Builder (but its not working for me for some reason)
    public CustomForm addComponent(FormComponent component) {
        components.add(component);
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = super.serialize();
        JsonArray content = new JsonArray();
        components.forEach((x) -> {
            content.add(x.serialize());
        });
        object.add("content", content);
        return object;
    }
}
