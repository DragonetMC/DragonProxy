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
    public float headYaw;
    public float pitch;
    public byte mode;
    public boolean onGround;

    public MovePlayerPacket(byte[] data) {
        this.setData(data);
    }

    public MovePlayerPacket() {
        mode = MODE_NORMAL;
    }

    public MovePlayerPacket(long eid, float x, float y, float z, float yaw, float headYaw, float pitch, boolean onGround) {
        this.eid = eid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.pitch = pitch;
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
            writer.writeVarLong(this.eid);
            writer.writeVector3f(x, y + 1.62f, z);
            writer.writeVector3f(yaw, headYaw, pitch);
            writer.writeByte(this.mode);
            writer.writeByte(this.onGround ? (byte) 1 : (byte) 0);
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
            reader.switchEndianness(); // enter LE
            this.x = reader.readFloat();
            this.y = reader.readFloat() - 1.62f;
            this.z = reader.readFloat();
            this.yaw = reader.readFloat();
            this.headYaw = reader.readFloat();
            this.pitch = reader.readFloat();
            reader.switchEndianness(); // out of LE
            this.mode = reader.readByte();
            this.onGround = reader.readByte() > 0;
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
