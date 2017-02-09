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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.UpdateBlock;
import sul.protocol.pocket100.types.BlockPosition;

public class PCMultiBlockChangePacketTranslator implements PCPacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerMultiBlockChangePacket packet) {
        UpdateBlock[] packets = new UpdateBlock[packet.getRecords().length];
        for (int i = 0; i < packets.length; i++) {
            packets[i] = new UpdateBlock();
            /*packets[i].flags = generalFlag;
            packets[i].blockData = (byte) (packet.getRecords()[i].getBlock().getData() & 0xFF);*/
            //TODO: flagsAndMeta is one field, figure it out
            packets[i].position = new BlockPosition(packet.getRecords()[i].getPosition().getX(), packet.getRecords()[i].getPosition().getY() & 0xFF, packet.getRecords()[i].getPosition().getZ());
            packets[i].block = ItemBlockTranslator.translateToPE(packet.getRecords()[i].getBlock().getId()) & 0xFF;
        }
        return fromSulPackets(packets);
    }

}
