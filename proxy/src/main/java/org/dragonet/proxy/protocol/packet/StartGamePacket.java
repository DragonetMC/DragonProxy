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
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class StartGamePacket extends PEPacket {

    public long eid;
    public long rtid;
    public float x;
    public float y;
    public float z;
    public int seed;
    public byte dimension;
    public int generator;
    public int gamemode;
    public int difficulty;
    public int spawnX;
    public int spawnY;
    public int spawnZ;

    public boolean achivementDisabled;
    public int staticTime;
    public boolean eduMode;
    public float rainLevel;
    public float lightningLevel;
    public boolean commandsEnabled;
    public boolean texturepackRequired;
    public String levelId;
    public String worldName;


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
            writer.writeVarLong(this.eid);
            writer.writeVarLong(this.rtid);
            writer.writeVector3f(x, y + 1.62f, z);
            writer.writeFloat(0f); //Should be in LE
            writer.writeFloat(0f); //Should be in LE
            writer.writeVarInt(this.seed);
            writer.writeVarInt(this.dimension);
            writer.writeVarInt(this.generator);
            writer.writeVarInt(this.gamemode);
            writer.writeVarInt(this.difficulty);
            writer.writeBlockCoords(spawnX, spawnY, spawnZ);
            writer.writeBoolean(this.achivementDisabled);
            writer.writeVarInt(staticTime);
            writer.writeBoolean(this.eduMode);
            writer.switchEndianness(); // enter LE
            writer.writeFloat(this.rainLevel);
            writer.writeFloat(this.lightningLevel);
            writer.switchEndianness(); // out of LE
            writer.writeBoolean(this.commandsEnabled);
            writer.writeBoolean(this.texturepackRequired);
            writer.writeString(this.levelId);
            writer.writeString(this.worldName);
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
