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

import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.BlockEntityDataPacket;
import org.dragonet.proxy.utilities.BlockPosition;

public class PCUpdateSignPacketTranslator implements IPCPacketTranslator<ServerUpdateTileEntityPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerUpdateTileEntityPacket packet) {
        if (packet.getType().equals(UpdatedTileType.SIGN)) {
            CompoundTag root = new CompoundTag(null);
            root.put(new StringTag("id", "Sign"));
            root.put(new IntTag("x", packet.getPosition().getX()));
            root.put(new IntTag("y", packet.getPosition().getY()));
            root.put(new IntTag("z", packet.getPosition().getZ()));
            root.put(new StringTag("Text", (String) packet.getNBT().get("Text1").getValue()
                + (String) packet.getNBT().get("Text2").getValue()
                + (String) packet.getNBT().get("Text3").getValue()
                + (String) packet.getNBT().get("Text4").getValue()));

            BlockEntityDataPacket data = new BlockEntityDataPacket();
            data.tag = ItemBlockTranslator.translateBlockEntityToPE(root);
            data.blockPosition = new BlockPosition(packet.getPosition());
            return new PEPacket[]{data};
        } else {
            return null;
        }
    }
}
