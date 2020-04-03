/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@Log4j2
public abstract class Form {
    private String type;
    private String title;

    public CompletableFuture<JsonArray> send(ProxySession session) {
        int id = session.getFormIdCounter().incrementAndGet();

        CompletableFuture<JsonArray> future = new CompletableFuture<>();
        session.getFormCache().put(id, future);

        ModalFormRequestPacket packet = new ModalFormRequestPacket();
        packet.setFormId(id);
        packet.setFormData(serialize().toString());

        session.sendPacket(packet);
        return future;
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("title", title);
        object.addProperty("type", type);
        return object;
    }
}
