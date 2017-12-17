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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator.ItemEntry;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateBlockPacket;
import org.dragonet.proxy.utilities.BlockPosition;

public class PCBlockChangePacketTranslator implements IPCPacketTranslator<ServerBlockChangePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerBlockChangePacket packet) {
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.flags = UpdateBlockPacket.FLAG_NEIGHBORS << 4;
        ItemEntry entry = ItemBlockTranslator.translateToPE(packet.getRecord().getBlock().getId(), packet.getRecord().getBlock().getData());

        pk.data = entry.damage;
        pk.id = entry.id;
        pk.blockPosition = new BlockPosition(packet.getRecord().getPosition().getX(),
                packet.getRecord().getPosition().getY(), packet.getRecord().getPosition().getZ());
        return new PEPacket[]{pk};
    }
}
