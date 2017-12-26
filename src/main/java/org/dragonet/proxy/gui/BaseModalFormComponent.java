package org.dragonet.proxy.gui;

import org.json.JSONObject;

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
	public final JSONObject serializeToJson() {
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		serializeData(obj);
		return obj;
	}
}
