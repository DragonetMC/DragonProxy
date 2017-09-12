/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket132.play.UpdateBlock;
import sul.protocol.pocket132.types.BlockPosition;
import sul.utils.Packet;

public class PCMultiBlockChangePacketTranslator implements PCPacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerMultiBlockChangePacket packet) {
        UpdateBlock[] packets = new UpdateBlock[packet.getRecords().length];
        int generalFlag = packet.getRecords().length > 64 ? UpdateBlock.PRIORITY : UpdateBlock.NEIGHBORS;
        for (int i = 0; i < packets.length; i++) {
            packets[i] = new UpdateBlock();
            packets[i].position = new BlockPosition(packet.getRecords()[i].getPosition().getX(), packet.getRecords()[i].getPosition().getY(), packet.getRecords()[i].getPosition().getZ());
            packets[i].block = (byte) (ItemBlockTranslator.translateToPE(packet.getRecords()[i].getBlock().getId()) & 0xFF);
            packets[i].flagsAndMeta = generalFlag << 4 | (packet.getRecords()[i].getBlock().getData() & 0xF);
        }
        return packets;
    }

}
