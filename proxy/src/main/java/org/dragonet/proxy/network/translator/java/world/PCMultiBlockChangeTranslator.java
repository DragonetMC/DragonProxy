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

import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.nukkitx.math.vector.Vector3i;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;


@PacketRegisterInfo(packet = ServerMultiBlockChangePacket.class)
public class PCMultiBlockChangeTranslator extends PacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public void translate(ProxySession session, ServerMultiBlockChangePacket packet) {
        for(BlockChangeRecord record : packet.getRecords()) {
            Vector3i position = Vector3i.from(record.getPosition().getX(), record.getPosition().getY(), record.getPosition().getZ());
            session.getChunkCache().updateBlock(session, position, record.getBlock());
        }
    }
}
