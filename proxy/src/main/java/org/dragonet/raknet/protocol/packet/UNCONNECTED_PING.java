package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX Nukkit Project
 */
public class UNCONNECTED_PING extends Packet {

    public static byte ID = (byte) 0x01;

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.put(RakNet.MAGIC);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
    }
}
