/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCMultiBlockChangeTranslator implements PacketTranslator<ServerMultiBlockChangePacket> {
    public static final PCMultiBlockChangeTranslator INSTANCE = new PCMultiBlockChangeTranslator();

    @Override
    public void translate(ProxySession session, ServerMultiBlockChangePacket packet) {
        for(BlockChangeRecord record : packet.getRecords()) {
            UpdateBlockPacket updateBlock = new UpdateBlockPacket();

            Position pos = record.getPosition();

            updateBlock.setBlockPosition(new Vector3i(pos.getX(), pos.getY(), pos.getY()));

            session.sendPacket(updateBlock);
        }
    }
}
