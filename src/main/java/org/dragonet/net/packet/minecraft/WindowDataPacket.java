/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class WindowDataPacket extends PEPacket {

    private byte windowID;
    private short property;
    private short value;

    @Override
    public int pid() {
        return PEPacketIDs.WINDOW_SET_DATA_PACKET;
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
            writer.writeShort(this.property);
            writer.writeShort(this.value);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
