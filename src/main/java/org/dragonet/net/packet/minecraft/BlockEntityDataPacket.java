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
import java.nio.ByteOrder;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.nbt.PENBT;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class BlockEntityDataPacket extends PEPacket {

    public int x;
    public int y;
    public int z;
    public CompoundTag peTag;

    public BlockEntityDataPacket() {
    }

    public BlockEntityDataPacket(int x, int y, int z, CompoundTag peTag) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.peTag = peTag;
    }

    @Override
    public int pid() {
        return PEPacketIDs.BLOCK_ENTITY_DATA_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(x);
            writer.writeInt(y);
            writer.writeInt(z);
            PENBT.write(peTag, bos, ByteOrder.LITTLE_ENDIAN);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
