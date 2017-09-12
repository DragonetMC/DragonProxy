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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.MessageTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket113.play.BlockEntityData;
import sul.utils.Packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PCUpdateSignPacketTranslator implements PCPacketTranslator<ServerUpdateTileEntityPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerUpdateTileEntityPacket packet) {
        CompoundTag root = new CompoundTag();
        root.putString("id", "Sign");
        root.putInt("x", packet.getPosition().getX());
        root.putInt("y", packet.getPosition().getY());
        root.putInt("z", packet.getPosition().getZ());
        /* root.putString("Text1", MessageTranslator.translate(packet.getLines()[0]));
        root.putString("Text2", MessageTranslator.translate(packet.getLines()[1]));
        root.putString("Text3", MessageTranslator.translate(packet.getLines()[2]));
        root.putString("Text4", MessageTranslator.translate(packet.getLines()[3])); */

        BlockEntityData data = new BlockEntityData();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            packet.getNBT().write(dos);
            dos.close();
            bos.close();
            data.nbt = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ(), root
        return new Packet[]{data};
    }

}
