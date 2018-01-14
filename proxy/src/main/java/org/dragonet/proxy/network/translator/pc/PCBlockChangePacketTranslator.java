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
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.UpdateBlockPacket;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.Position;

public class PCBlockChangePacketTranslator implements IPCPacketTranslator<ServerBlockChangePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerBlockChangePacket packet) {
        ItemEntry entry = ItemBlockTranslator.translateToPE(packet.getRecord().getBlock().getId(), packet.getRecord().getBlock().getData());
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.flags = UpdateBlockPacket.FLAG_NEIGHBORS;
        pk.data = entry.getPEDamage();
        pk.id = entry.getId();
        pk.blockPosition = new BlockPosition(packet.getRecord().getPosition());

        // Save glitchy items in cache
        Position blockPosition = new Position(pk.blockPosition.x, pk.blockPosition.y, pk.blockPosition.z);
        session.getBlockCache().checkBlock(entry.getId(), entry.getPEDamage(), blockPosition);

        return new PEPacket[]{pk};
    }
}
