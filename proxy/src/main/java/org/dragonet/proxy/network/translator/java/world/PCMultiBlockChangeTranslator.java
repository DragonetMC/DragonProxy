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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.BlockTranslator;


@PCPacketTranslator(packetClass = ServerMultiBlockChangePacket.class)
public class PCMultiBlockChangeTranslator extends PacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public void translate(ProxySession session, ServerMultiBlockChangePacket packet) {
        for(BlockChangeRecord record : packet.getRecords()) {
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setRuntimeId(BlockTranslator.translateToBedrock(record.getBlock()));
            updateBlockPacket.setBlockPosition(Vector3i.from(record.getPosition().getX(), record.getPosition().getY(), record.getPosition().getZ()));
            updateBlockPacket.getFlags().add(UpdateBlockPacket.Flag.NEIGHBORS);
            updateBlockPacket.setDataLayer(0);

            session.sendPacket(updateBlockPacket);

            // TODO: waterlogged, update in chunk cache?
        }
    }
}
