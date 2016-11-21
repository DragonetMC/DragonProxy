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

public class PlayerActionPacket extends PEPacket {

    public static final byte ACTION_START_BREAK = 0;
    public static final byte ACTION_CANCEL_BREAK = 1;
    public static final byte ACTION_FINISH_BREAK = 2;
    public static final byte ACTION_RELEASE_ITEM = 5;
    public static final byte ACTION_STOP_SLEEPING = 6;
    public static final byte ACTION_RESPAWN = 7;
    public static final byte ACTION_JUMP = 8;
    public static final byte ACTION_START_SPRINT = 9;
    public static final byte ACTION_STOP_SPRINT = 10;
    public static final byte ACTION_START_SNEAK = 11;
    public static final byte ACTION_STOP_SNEAK = 12;
    public static final byte ACTION_DIMENSION_CHANGE = 13;

    public long eid;
    public int action;
    public int x;
    public int y;
    public int z;
    public int face;

    public PlayerActionPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.PLAYER_ACTION_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeVarLong(eid);
            writer.writeVarInt(action);
            writer.writeBlockCoords(x, y, z);
            writer.writeVarInt(face);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            eid = reader.readLong();
            action = reader.readInt();
            x = reader.readInt();
            y = reader.readInt();
            z = reader.readInt();
            face = reader.readInt();
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
