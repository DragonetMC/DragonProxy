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
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class ChangeDimensionPacket extends PEPacket {

    public byte dimension;
    public float x;
    public float y;
    public float z;
    public byte unknown;

    public ChangeDimensionPacket(byte dimension, int x, int y, int z, byte unknown) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.unknown = unknown;
    }
    
    @Override
    public int pid() {
        return PEPacketIDs.CHANGE_DIMENSION_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeByte(dimension);
            writer.writeFloat(x);
            writer.writeFloat(y);
            writer.writeFloat(z);
            writer.writeByte(unknown);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
