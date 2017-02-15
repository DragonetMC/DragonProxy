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
import java.util.ArrayList;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class WindowItemsPacket extends PEPacket {

    public final static WindowItemsPacket CREATIVE_INVENTORY;

    public byte windowID;
    public PEInventorySlot[] slots;
    public int[] hotbar;

    @Override
    public int pid() {
        return PEPacketIDs.WINDOW_SET_CONTENT_PACKET;
    }

    @Override
    public void encode() {
        setShouldSendImmidate(true);
        try {
            setChannel(NetworkChannel.CHANNEL_PRIORITY);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeByte(this.windowID);
            writer.writeUnsignedVarInt(this.slots.length);
            for (PEInventorySlot slot : this.slots) {
                PEInventorySlot.writeSlot(writer, slot);
            }
            if (hotbar != null && windowID == PEWindowConstantID.PLAYER_INVENTORY && hotbar.length > 0) {
                writer.writeUnsignedVarInt(this.hotbar.length & 0xFFFF);
                for (int slot : hotbar) {
                    writer.writeVarInt(slot);
                }
            } else {
                writer.writeUnsignedVarInt(0);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.windowID = reader.readByte();
            int cnt = reader.readUnsignedVarInt();
            slots = new PEInventorySlot[cnt];
            for (int i = 0; i < cnt; i++) {
                slots[i] = PEInventorySlot.readSlot(reader);
            }
            if (this.windowID == PEWindowConstantID.PLAYER_INVENTORY) {
                int hcnt = reader.readUnsignedVarInt();
                hotbar = new int[hcnt];
                for (int i = 0; i < hcnt; i++) {
                    hotbar[i] = reader.readVarInt();
                }
            }
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

    static {
        CREATIVE_INVENTORY = new WindowItemsPacket();
        CREATIVE_INVENTORY.windowID = PEWindowConstantID.PLAYER_CREATIVE;
        ArrayList<PEInventorySlot> slots = new ArrayList<>();
        /*
        for(Material mat : Material.values()){
            for(int i = 0; i < mat.getMaxDurability() && i < 16; i++){
                slots.add(new PEInventorySlot((short)mat.getId(), (byte)1, (short)(i & 0xFF)));
            }
        }
         */
        //HACK
        slots.add(new PEInventorySlot((short) 1, (byte) 1, (short) 0));
        CREATIVE_INVENTORY.slots = slots.toArray(new PEInventorySlot[0]);
    }
}
