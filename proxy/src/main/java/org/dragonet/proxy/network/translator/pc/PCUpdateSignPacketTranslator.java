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

import java.io.IOException;
import java.nio.ByteOrder;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import org.spacehq.mc.protocol.data.message.Message;

public class PCUpdateSignPacketTranslator implements PCPacketTranslator<ServerUpdateTileEntityPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerUpdateTileEntityPacket packet) {
        CompoundTag root = new CompoundTag();
        root.putString("id", "Sign");
        root.putInt("x", packet.getPosition().getX());
        root.putInt("y", packet.getPosition().getY());
        root.putInt("z", packet.getPosition().getZ());
        root.putString("Text1", MessageTranslator.translate(packet.getNBT().get("Text1")));
        root.putString("Text2", MessageTranslator.translate(packet.getNBT().get("Text2")));
        root.putString("Text3", MessageTranslator.translate(packet.getNBT().get("Text3")));
        root.putString("Text4", MessageTranslator.translate(packet.getNBT().get("Text4")));
        
        
        BlockEntityDataPacket pkBlockData = new BlockEntityDataPacket();
        pkBlockData.x = packet.getPosition().getX();
        pkBlockData.y = packet.getPosition().getY();
        pkBlockData.z = packet.getPosition().getZ();
        try {
			pkBlockData.namedTag = NBTIO.write(root, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return new DataPacket[]{pkBlockData};
    }

}
