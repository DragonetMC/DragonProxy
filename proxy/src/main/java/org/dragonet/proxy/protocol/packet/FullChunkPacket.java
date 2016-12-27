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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class FullChunkPacket extends PEPacket {

    public int chunkX;
    public int chunkZ;
    public ChunkOrder order;
    public byte[] chunkData;

    public enum ChunkOrder {
        COLUMNS((byte) 0),
        LAYERS((byte) 1);
        private byte type;

        ChunkOrder(byte t) {
            this.type = t;
        }

        public byte getType() {
            return type;
        }
    }

    @Override
    public int pid() {
        return PEPacketIDs.FULL_CHUNK_DATA_PACKET;
    }

    @Override
    public void encode() {
        try {
            setShouldSendImmidate(false);

            setChannel(NetworkChannel.CHANNEL_WORLD_CHUNKS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeVarInt(chunkX);
            writer.writeVarInt(chunkZ);
            writer.writeByte(order != null ? order.getType() : (byte) 0); //Default to COLUMNS
            writer.writeUnsignedVarInt(chunkData.length);
            writer.write(chunkData);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
