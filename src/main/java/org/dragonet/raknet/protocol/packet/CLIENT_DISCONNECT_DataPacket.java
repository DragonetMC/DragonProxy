package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX Nukkit Project
 */
public class CLIENT_DISCONNECT_DataPacket extends Packet {

    public static byte ID = (byte) 0x15;

    @Override
    public byte getID() {
        return ID;
    }
}
