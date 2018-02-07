package org.dragonet.common.gui;

import com.google.gson.JsonObject;

/**
 * Created on 2017/12/26.
 */
public abstract class BaseModalFormComponent implements ModalFormComponent {

    private final String type;

    public BaseModalFormComponent(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public final JsonObject serializeToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", type);
        serializeData(obj);
        return obj;
    }
}
