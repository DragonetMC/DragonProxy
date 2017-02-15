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
package org.dragonet.proxy.protocol.packet;

import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MoveEntitiesPacket extends PEPacket {

    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float headYaw;
    public float pitch;

    public MoveEntitiesPacket() {
    }

    @Override
    public int pid() {
        return PEPacketIDs.MOVE_ENTITY_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeVarLong(eid);
            writer.writeVector3f(x, y, z);
            writer.writeByte((byte) (this.pitch / (360d / 256d)));
            writer.writeByte((byte) (this.headYaw / (360d / 256d)));
            writer.writeByte((byte) (this.yaw / (360d / 256d)));
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
