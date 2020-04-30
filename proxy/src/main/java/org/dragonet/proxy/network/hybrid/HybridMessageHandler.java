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
package org.dragonet.proxy.network.hybrid;

import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.hybrid.AbstractHybridMessageHandler;
import org.dragonet.proxy.hybrid.messages.CommandsMessage;
import org.dragonet.proxy.hybrid.messages.EncryptionMessage;
import org.dragonet.proxy.hybrid.messages.SetEntityDataMessage;
import org.dragonet.proxy.hybrid.messages.ShowFormMessage;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

@Log4j2
@RequiredArgsConstructor
public class HybridMessageHandler implements AbstractHybridMessageHandler {
    private final ProxySession session;

    // Bedrock to remote
    @Getter
    private final Int2IntMap formIdMap = new Int2IntOpenHashMap();

    // TODO: move to PlayerLoginMessage?
    @Override
    public void handle(EncryptionMessage message) {
        if(message.isEncryptionEnabled() && session.getProxy().getConfiguration().getHybridConfig().isEncryption()) {
            log.warn("Hybrid encryption enabled!");
        }
    }

    @Override
    public void handle(CommandsMessage message) {

    }

    @Override
    public void handle(SetEntityDataMessage message) {
        CachedEntity entity = session.getEntityCache().getByRemoteId(message.getEntityId());
        if(entity == null) {
            log.warn("Received " + message.getId() + " from " + session.getUsername() + ", entity was null");
            return;
        }

//        entity.getMetadata().putAll(message.getEntityDataMap());
//        entity.sendMetadata(session);
    }

    @Override
    public void handle(ShowFormMessage message) {
        int proxyId = session.getFormIdCounter().getAndIncrement();
        formIdMap.put(proxyId, message.getFormId());

        ModalFormRequestPacket modalFormRequestPacket = new ModalFormRequestPacket();
        modalFormRequestPacket.setFormId(proxyId);
        modalFormRequestPacket.setFormData(message.getFormData());

        session.sendPacket(modalFormRequestPacket);
    }
}
