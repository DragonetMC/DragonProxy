/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;

public class DropItemPacket extends PEPacket {

    public DropItemPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.DROP_ITEM_PACKET;
    }

    public byte type;
    public PEInventorySlot slot;

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            setChannel(NetworkChannel.CHANNEL_ENTITY_SPAWNING);
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.type = reader.readByte();
            this.slot = PEInventorySlot.readSlot(reader);
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
