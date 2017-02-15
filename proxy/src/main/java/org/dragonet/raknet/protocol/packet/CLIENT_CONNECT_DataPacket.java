package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX Nukkit Project
 */
public class CLIENT_CONNECT_DataPacket extends Packet {

    public static byte ID = (byte) 0x09;

    @Override
    public byte getID() {
        return ID;
    }

    public long clientID;
    public long sendPing;
    public boolean useSecurity = false;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.clientID);
        this.putLong(this.sendPing);
        this.putByte((byte) (this.useSecurity ? 1 : 0));
    }

    @Override
    public void decode() {
        super.decode();
        this.clientID = this.getLong();
        this.sendPing = this.getLong();
        this.useSecurity = this.getByte() > 0;
    }
}
