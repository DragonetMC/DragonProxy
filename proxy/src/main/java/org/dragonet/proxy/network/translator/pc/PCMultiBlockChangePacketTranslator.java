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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.UpdateBlockPacket;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.ChunkPos;

public class PCMultiBlockChangePacketTranslator implements IPCPacketTranslator<ServerMultiBlockChangePacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerMultiBlockChangePacket packet) {
        UpdateBlockPacket[] packets = new UpdateBlockPacket[packet.getRecords().length];
        // int generalFlag = packet.getRecords().length > 64 ?
        // UpdateBlockPacket.FLAG_ALL_PRIORITY : UpdateBlockPacket.FLAG_NEIGHBORS;
        for (int i = 0; i < packets.length; i++) {
            Position pos = packet.getRecords()[i].getPosition();
            BlockState block = packet.getRecords()[i].getBlock();
            try {
                ChunkPos chunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
                session.getChunkCache().update(pos, block);

                ItemEntry entry = session.getChunkCache().translateBlock(pos);

                if (entry == null) {
                    entry = ItemBlockTranslator.translateToPC(block.getId(), block.getData());
                }
                packets[i] = new UpdateBlockPacket();
                packets[i].blockPosition = new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
                packets[i].id = entry.getId();
                packets[i].flags = UpdateBlockPacket.FLAG_NEIGHBORS;
                packets[i].data = entry.getPEDamage();
            } catch (Exception ex) {
                session.getProxy().getLogger().debug("Error when updating block [" + pos.getX() + "," + pos.getY() + ","
                        + pos.getZ() + "] " + block.toString());
                session.getProxy().getLogger().debug(ex.getMessage());
            }

            // Save glitchy items in cache
            // Position blockPosition = new Position(packets[i].blockPosition.x,
            // packets[i].blockPosition.y, packets[i].blockPosition.z);
        }
        return packets;
    }
}
