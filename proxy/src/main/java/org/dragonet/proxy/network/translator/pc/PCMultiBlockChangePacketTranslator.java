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

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;

public class PCMultiBlockChangePacketTranslator implements PCPacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerMultiBlockChangePacket packet) {
        UpdateBlockPacket[] packets = new UpdateBlockPacket[packet.getRecords().length];
        byte generalFlag = (byte) (packet.getRecords().length > 64 ? UpdateBlockPacket.FLAG_PRIORITY : UpdateBlockPacket.FLAG_ALL);
        for (int i = 0; i < packets.length; i++) {
            packets[i] = new UpdateBlockPacket();
            packets[i].flags = generalFlag;
            packets[i].x = packet.getRecords()[i].getPosition().getX();
            packets[i].y = (byte) (packet.getRecords()[i].getPosition().getY() & 0xFF);
            packets[i].z = packet.getRecords()[i].getPosition().getZ();
            packets[i].blockId = (byte) (ItemBlockTranslator.translateToPE(packet.getRecords()[i].getBlock().getId()) & 0xFF);
            packets[i].blockData = (byte) (packet.getRecords()[i].getBlock().getData() & 0xFF);
        }
        return packets;
    }

}
