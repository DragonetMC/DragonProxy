package org.dragonet.proxy.gui;

import org.json.JSONObject;

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
	public void serializeData(JSONObject out) {
		out.put("text", text);
	}
}
