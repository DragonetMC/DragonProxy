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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.BlockEntityData;
import sul.protocol.pocket100.types.BlockPosition;

public class PCUpdateSignPacketTranslator implements PCPacketTranslator<ServerUpdateTileEntityPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerUpdateTileEntityPacket packet) {
        CompoundTag root = new CompoundTag();
        root.putString("id", "Sign");
        root.putInt("x", packet.getPosition().getX());
        root.putInt("y", packet.getPosition().getY());
        root.putInt("z", packet.getPosition().getZ());
        root.putString("Text1", packet.getNBT().get("Text1").toString());
        root.putString("Text2", packet.getNBT().get("Text2").toString());
        root.putString("Text3", packet.getNBT().get("Text3").toString());
        root.putString("Text4", packet.getNBT().get("Text4").toString());


        BlockEntityData pkBlockData = new BlockEntityData();
        pkBlockData.position = new BlockPosition(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        try {
			pkBlockData.nbt = NBTIO.write(root, ByteOrder.LITTLE_ENDIAN, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

        return fromSulPackets(pkBlockData);
    }

}
