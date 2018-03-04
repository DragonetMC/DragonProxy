package org.dragonet.common.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/12/26.
 */
public class CustomFormComponent extends BaseModalFormComponent {

    private String title;

    private final List<ModalFormComponent> components = new ArrayList<>();

    public CustomFormComponent(String title) {
        super("custom_form");
        this.title = title;
    }

    public CustomFormComponent addComponent(ModalFormComponent component) {
        components.add(component);
        return this;
    }

    public List<ModalFormComponent> getComponents() {
        return components;
    }

    @Override
    public void serializeData(JsonObject out) {
        out.addProperty("title", title);
        JsonArray content = new JsonArray();
        components.forEach((c) -> content.add(c.serializeToJson()));
        out.add("content", content);
    }
}
