package org.dragonet.common.gui;

import com.google.gson.JsonObject;

/**
 * Created on 2017/12/26.
 */
public class LabelComponent extends BaseModalFormComponent {

    private String text;

    public LabelComponent(String text) {
        super("label");
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void serializeData(JsonObject out) {
        out.addProperty("text", text);
    }
}
