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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerActionAckPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayerActionAckPacket.class)
public class PCPlayerActionAckTranslator extends PacketTranslator<ServerPlayerActionAckPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerActionAckPacket packet) {
        if(!packet.isSuccessful()) {
            log.info(TextFormat.GRAY + "(debug) Player action not successful: " + packet.getAction().name());
            //return;
        }

        LevelEventPacket levelEventPacket = new LevelEventPacket();
        Vector3f position = Vector3f.from(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());

        switch(packet.getAction()) {
            case START_DIGGING:
                // TODO: ServerBlockBreakAnimPacket?
                levelEventPacket.setType(LevelEventType.BLOCK_START_BREAK);
                levelEventPacket.setPosition(position);
                levelEventPacket.setData((int) (65535 / Math.ceil(0.5 * 20))); // TODO: break times
                break;
            case CANCEL_DIGGING:
                levelEventPacket.setType(LevelEventType.BLOCK_STOP_BREAK);
                levelEventPacket.setPosition(position);
                levelEventPacket.setData(0);

                session.sendPacket(levelEventPacket);
                break;
            case FINISH_DIGGING:
                session.getChunkCache().updateBlock(session, position.toInt(), packet.getNewState());
                break;
        }
    }
}
