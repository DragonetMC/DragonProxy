package org.dragonet.common.gui;

import com.google.gson.JsonObject;

/**
 * Created on 2017/12/26.
 */
public interface ModalFormComponent {

    String getType();

    void serializeData(JsonObject out);

    JsonObject serializeToJson();

}
