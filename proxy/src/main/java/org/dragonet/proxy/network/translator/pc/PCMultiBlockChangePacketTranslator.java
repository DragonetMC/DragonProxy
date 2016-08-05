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

import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.protocol.packet.UpdateBlockPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

public class PCMultiBlockChangePacketTranslator implements PCPacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerMultiBlockChangePacket packet) {
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.records = new UpdateBlockPacket.UpdateBlockRecord[packet.getRecords().length];
        byte generalFlag = packet.getRecords().length > 64 ? UpdateBlockPacket.FLAG_PRIORITY : UpdateBlockPacket.FLAG_ALL;
        for (int i = 0; i < pk.records.length; i++) {
            pk.records[i] = new UpdateBlockPacket.UpdateBlockRecord();
            pk.records[i].flags = generalFlag;
            pk.records[i].x = packet.getRecords()[i].getPosition().getX();
            pk.records[i].y = (byte) (packet.getRecords()[i].getPosition().getY() & 0xFF);
            pk.records[i].z = packet.getRecords()[i].getPosition().getZ();
            pk.records[i].block = (byte) (ItemBlockTranslator.translateToPE(packet.getRecords()[i].getId()) & 0xFF);
            pk.records[i].meta = (byte) (packet.getRecords()[i].getData() & 0xFF);
        }
        return new PEPacket[]{pk};
    }

}
