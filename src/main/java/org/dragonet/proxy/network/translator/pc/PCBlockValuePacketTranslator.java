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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.data.itemsblocks.ItemEntry;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateBlockPacket;
import org.dragonet.proxy.utilities.BlockPosition;

public class PCBlockValuePacketTranslator implements IPCPacketTranslator<ServerBlockValuePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerBlockValuePacket packet) {

// Used for updating these blocks
//NOTE_BLOCK = 25; //NoteBlockValue //int pitch
//STICKY_PISTON = 29; //PistonValue // DOWN, UP, SOUTH, WEST, NORTH, EAST;
//PISTON = 33;
//MOB_SPAWNER = 52; //MobSpawnerValue //??
//CHEST = 54; //ChestValue //int viewers
//ENDER_CHEST = 130;
//TRAPPED_CHEST = 146;
//SHULKER_BOX_LOWER = 219;
//SHULKER_BOX_HIGHER = 234;

//        UpdateBlockPacket pk = new UpdateBlockPacket();
//        pk.flags = UpdateBlockPacket.FLAG_NEIGHBORS << 4;
//        ItemEntry entry = ItemBlockTranslator.translateToPE(packet.getBlockId(), packet.getValue());
//
//        pk.data = entry.damage;
//        pk.id = entry.id;
//        pk.blockPosition = new BlockPosition(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        return new PEPacket[]

            {
            }
            ;
    }
}
