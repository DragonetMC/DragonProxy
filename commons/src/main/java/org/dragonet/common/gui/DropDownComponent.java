package org.dragonet.common.gui;

import com.google.gson.JsonObject;
import org.dragonet.common.utilities.JsonUtil;

import java.util.List;

/**
 * Created on 2017/12/26.
 */
public class DropDownComponent extends BaseModalFormComponent {

    private String text;
    private List<String> options;

    public DropDownComponent(String text, List<String> options) {
        super("dropdown");
        this.text = text;
        this.options = options;
    }

    @Override
    public void serializeData(JsonObject out) {
        out.addProperty("text", text);
        out.add("options", JsonUtil.toArray(options));
    }
}
