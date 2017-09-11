package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.DataPacket;
import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_F extends DataPacket {
    public static final byte ID = (byte) 0x8f;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements PacketFactory {

        @Override
        public Packet create() {
            return new DATA_PACKET_F();
        }

    }

}
