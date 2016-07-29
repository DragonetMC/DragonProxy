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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class StartGamePacket extends PEPacket {

    public int seed;
    public byte dimension;
    public int generator;
    public int gamemode;
    public long eid;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public float x;
    public float y;
    public float z;

    @Override
    public int pid() {
        return PEPacketIDs.START_GAME_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(this.seed);
            writer.writeByte(this.dimension);
            writer.writeInt(this.generator);
            writer.writeInt(this.gamemode);
            writer.writeLong(this.eid);
            writer.writeInt(this.spawnX);
            writer.writeInt(this.spawnY);
            writer.writeInt(this.spawnZ);
            writer.writeFloat(this.x);
            writer.writeFloat(this.y + 1.62f);
            writer.writeFloat(this.z);
            writer.writeByte((byte) 0x01); //userPerm
            writer.writeByte((byte) 0x01); //globalPerm
            writer.writeByte((byte) 0x00);
            writer.writeString(""); //Unknown string
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            seed = reader.readInt();
            dimension = reader.readByte();
            generator = reader.readInt();
            gamemode = reader.readInt();
            eid = reader.readLong();
            spawnX = reader.readInt();
            spawnY = reader.readInt();
            spawnZ = reader.readInt();
            x = reader.readFloat();
            y = reader.readFloat() - 1.62f;
            z = reader.readFloat();
            // Ignore the rest of the packet
            // ...
        } catch (IOException e) {
        }
    }

}
