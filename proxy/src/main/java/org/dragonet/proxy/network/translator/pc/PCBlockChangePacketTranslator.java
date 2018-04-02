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
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.protocol.packets.UpdateBlockPacket;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.Vector3F;

public class PCBlockChangePacketTranslator implements IPCPacketTranslator<ServerBlockChangePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerBlockChangePacket packet) {
        Position pos = packet.getRecord().getPosition();
        if(packet.getRecord().getBlock().getId() == 0 && session.getChunkCache().getBlock(pos).getId() != 0) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_DESTROY;
            pk.position = new Vector3F(pos.getX(), pos.getY(), pos.getZ());
            pk.data = session.getChunkCache().getBlock(pos).getId();
            session.sendPacket(pk);
        }
        //update cache
        session.getChunkCache().update(pos, packet.getRecord().getBlock());

        // Save glitchy items in cache
//        Position blockPosition = new Position(pk.blockPosition.x, pk.blockPosition.y, pk.blockPosition.z);
//        session.getBlockCache().checkBlock(entry.getId(), entry.getPEDamage(), blockPosition);
        ItemEntry entry = session.getChunkCache().translateBlock(pos);
        if (entry != null) {
            UpdateBlockPacket pk = new UpdateBlockPacket();
            pk.flags = UpdateBlockPacket.FLAG_NEIGHBORS;
            pk.data = entry.getPEDamage();
            pk.id = entry.getId();
            pk.blockPosition = new BlockPosition(pos);
            session.putCachePacket(pk);
        }
        return null;
    }
}
