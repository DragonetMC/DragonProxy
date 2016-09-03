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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class MovePlayerPacket extends PEPacket {

    public static final byte MODE_NORMAL = 0;
    public static final byte MODE_RESET = 1;
    public static final byte MODE_ROTATION = 2;

    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float pitch;
    public float bodyYaw;
    public byte mode;
    public boolean onGround;

    public MovePlayerPacket(byte[] data) {
        this.setData(data);
    }

    public MovePlayerPacket() {
        mode = MODE_NORMAL;
    }

    public MovePlayerPacket(long eid, float x, float y, float z, float yaw, float pitch, float bodyYaw, boolean onGround) {
        this.eid = eid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.bodyYaw = bodyYaw;
        this.onGround = onGround;
        mode = MODE_NORMAL;
    }

    @Override
    public int pid() {
        return PEPacketIDs.MOVE_PLAYER_PACKET;
    }

    @Override
    public void encode() {
        setShouldSendImmidate(true);
        try {
            setChannel(NetworkChannel.CHANNEL_MOVEMENT);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(this.eid);
            writer.writeFloat(this.x);
            writer.writeFloat(this.y + 1.62f);
            writer.writeFloat(this.z);
            writer.writeFloat(this.yaw);
            writer.writeFloat(this.bodyYaw);
            writer.writeFloat(this.pitch);
            writer.writeByte(this.mode);
            writer.writeByte((byte) (this.onGround ? (byte) 1 : (byte) 0));
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.eid = reader.readLong();
            this.x = reader.readFloat();
            this.y = reader.readFloat() - 1.62f;
            this.z = reader.readFloat();
            this.yaw = reader.readFloat();
            this.bodyYaw = reader.readFloat();
            this.pitch = reader.readFloat();
            this.mode = reader.readByte();
            this.onGround = reader.readByte() > 0;
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
