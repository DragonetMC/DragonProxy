package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX Nukkit Project
 */
public class UNCONNECTED_PONG extends Packet {

    public static byte ID = (byte) 0x1c;

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;
    public long serverID;
    public String serverName;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.putLong(this.serverID);
        this.put(RakNet.MAGIC);
        this.putString(this.serverName);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
        this.serverID = this.getLong();
        this.offset += 16; //skip magic bytes todo:check magic?
        this.serverName = this.getString();
    }
}
