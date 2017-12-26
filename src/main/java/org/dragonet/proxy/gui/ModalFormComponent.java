package org.dragonet.proxy.gui;

import org.json.JSONObject;

/**
 * Created on 2017/12/26.
 */
public interface ModalFormComponent {

	String getType();

	void serializeData(JSONObject out);

	JSONObject serializeToJson();

}
