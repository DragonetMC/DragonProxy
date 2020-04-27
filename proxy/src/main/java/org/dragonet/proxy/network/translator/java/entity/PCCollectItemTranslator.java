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
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = ServerEntityCollectItemPacket.class)
public class PCCollectItemTranslator extends PacketTranslator<ServerEntityCollectItemPacket> {

    @Override
    public void translate(ProxySession session, ServerEntityCollectItemPacket packet) {
        CachedEntity collector = session.getEntityCache().getByRemoteId(packet.getCollectorEntityId());
        CachedEntity item = session.getEntityCache().getByRemoteId(packet.getCollectedEntityId());

        TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
        takeItemEntityPacket.setRuntimeEntityId(collector.getProxyEid());
        takeItemEntityPacket.setItemRuntimeEntityId(item.getProxyEid());

        session.sendPacket(takeItemEntityPacket);
    }
}
