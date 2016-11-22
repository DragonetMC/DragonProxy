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

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResourcePackInfoPacket extends PEPacket {

    public boolean mustAccept = false;
    public ResourcePackInfoEntry[] behaviourPackEntries = new ResourcePackInfoEntry[0];
    public ResourcePackInfoEntry[] resourcePackEntries = new ResourcePackInfoEntry[0];

    @Override
    public int pid() {
        return PEPacketIDs.RESOURCE_PACKS_INFO_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeBoolean(mustAccept);
            for (ResourcePackInfoEntry entry : this.behaviourPackEntries) {
                writer.writeString(entry.getPackId());
                writer.writeString(entry.getVersion());
                writer.writeLong(entry.getPackSize());
            }
            writer.writeShort((short) (this.resourcePackEntries.length & 0xFFFF));
            for (ResourcePackInfoEntry entry : this.resourcePackEntries) {
                writer.writeString(entry.getPackId());
                writer.writeString(entry.getVersion());
                writer.writeLong(entry.getPackSize());
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {

    }

    @Data
    @AllArgsConstructor
    public static class ResourcePackInfoEntry {
        public final String packId;
        public final String version;
        public final long packSize;
    }
}
