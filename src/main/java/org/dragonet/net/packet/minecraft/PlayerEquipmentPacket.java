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
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class PlayerEquipmentPacket extends PEPacket {

    public long eid;
    public PEInventorySlot item;
    public int slot;
    public int selectedSlot;

    public PlayerEquipmentPacket() {
    }

    public PlayerEquipmentPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.MOB_EQUIPMENT_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(eid);
            PEInventorySlot.writeSlot(writer, item);
            writer.writeByte((byte) (slot & 0xFF));
            writer.writeByte((byte) (selectedSlot & 0xFF));
            this.setData(bos.toByteArray());
        } catch (IOException e) {

        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.eid = reader.readLong();
            this.item = PEInventorySlot.readSlot(reader);
            this.slot = reader.readByte() & 0xFF;
            this.selectedSlot = reader.readByte() & 0xFF;
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
