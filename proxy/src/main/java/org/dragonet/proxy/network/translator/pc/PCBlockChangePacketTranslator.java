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
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;

public class PCBlockChangePacketTranslator implements PCPacketTranslator<ServerBlockChangePacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerBlockChangePacket packet) {
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.flags = UpdateBlockPacket.FLAG_ALL;
        pk.blockId = (byte) (ItemBlockTranslator.translateToPE(packet.getRecord().getBlock().getId()) & 0xFF);
        pk.blockData = (byte) (packet.getRecord().getBlock().getData() & 0xFF);
        pk.x = packet.getRecord().getPosition().getX();
        pk.y = (byte) (packet.getRecord().getPosition().getY() & 0xFF);
        pk.z = packet.getRecord().getPosition().getZ();
        return new DataPacket[]{pk};
    }

}
