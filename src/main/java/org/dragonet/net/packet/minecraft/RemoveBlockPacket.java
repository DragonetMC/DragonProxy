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
import java.io.IOException;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;

public class RemoveBlockPacket extends PEPacket {

    public long eid;
    public int x;
    public int z;
    public int y;

    public RemoveBlockPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.REMOVE_BLOCK_PACKET;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.eid = reader.readLong();
            this.x = reader.readInt();
            this.z = reader.readInt();
            this.y = reader.readByte();
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
