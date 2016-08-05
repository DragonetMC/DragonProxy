package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.AcknowledgePacket;

/**
 * author: MagicDroidX Nukkit Project
 */
public class ACK extends AcknowledgePacket {

    public static byte ID = (byte) 0xc0;

    @Override
    public byte getID() {
        return ID;
    }
}
