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
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.UpdateBlock;
import sul.protocol.pocket101.types.BlockPosition;

public class PCBlockChangePacketTranslator implements PCPacketTranslator<ServerBlockChangePacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerBlockChangePacket packet) {
        UpdateBlock pk = new UpdateBlock();
        pk.flagsAndMeta = UpdateBlock.PRIORITY;
        pk.block = (byte) (ItemBlockTranslator.translateToPE(packet.getRecord().getBlock().getId()) & 0xFF);
        //TODO: pk.blockData = (byte) (packet.getRecord().getBlock().getData() & 0xFF);
        pk.position = new BlockPosition(packet.getRecord().getPosition().getX(), (byte) (packet.getRecord().getPosition().getY() & 0xFF), packet.getRecord().getPosition().getZ());
        
        return new sul.utils.Packet[] {pk};
    }

}
