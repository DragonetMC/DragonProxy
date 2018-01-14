package org.dragonet.common.gui;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public void serializeData(JSONObject out) {
        out.put("text", text);
        out.put("options", new JSONArray(options));
    }
}
