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
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewPositionPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = ServerUpdateViewPositionPacket.class)
public class PCUpdateViewPositionTranslator extends PacketTranslator<ServerUpdateViewPositionPacket> {

    @Override
    public void translate(ProxySession session, ServerUpdateViewPositionPacket packet) {
//        NetworkChunkPublisherUpdatePacket chunkPublisherUpdatePacket = new NetworkChunkPublisherUpdatePacket();
//        chunkPublisherUpdatePacket.setPosition(Vector3i.from(packet.getChunkX() << 4, 0, packet.getChunkZ() << 4));
//        chunkPublisherUpdatePacket.setRadius(session.getRenderDistance() << 4);
//        session.sendPacket(chunkPublisherUpdatePacket);
    }
}
