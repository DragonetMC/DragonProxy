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
import java.io.IOException;
import java.util.UUID;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.proxy.utilities.io.PEBinaryReader;

public class CraftingEventPacket extends PEPacket {

    public byte windowId;

    public int craftType;

    public UUID uuid;

    public PEInventorySlot[] input;

    public PEInventorySlot[] output;

    @Override
    public int pid() {
        return PEPacketIDs.CRAFTING_EVENT_PACKET;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.windowId = reader.readByte();
            this.craftType = reader.readInt();
            this.uuid = reader.readUUID();

            int size = reader.readInt();
            input = new PEInventorySlot[size > 128 ? 128 : size];
            for (int i = 0; i < size; i++) {
                input[i] = PEInventorySlot.readSlot(reader);
            }

            size = reader.readInt();
            output = new PEInventorySlot[size > 128 ? 128 : size];
            for (int i = 0; i < size; i++) {
                output[i] = PEInventorySlot.readSlot(reader);
            }

            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
