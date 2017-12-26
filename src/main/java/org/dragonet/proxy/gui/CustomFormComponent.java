package org.dragonet.proxy.gui;

import org.json.JSONArray;
import org.json.JSONObject;

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

	public CustomFormComponent addComponent(ModalFormComponent component){
		components.add(component);
		return this;
	}

	public List<ModalFormComponent> getComponents() {
		return components;
	}

	@Override
	public void serializeData(JSONObject out) {
		out.put("title", title);
		JSONArray content = new JSONArray();
		components.forEach((c) -> content.put(c.serializeToJson()));
		out.put("content", content);
	}
}
