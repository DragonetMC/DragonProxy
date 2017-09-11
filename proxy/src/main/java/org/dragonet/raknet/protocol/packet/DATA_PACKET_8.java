package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.DataPacket;
import org.dragonet.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_8 extends DataPacket {
    public static final byte ID = (byte) 0x88;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new DATA_PACKET_8();
        }

    }

}
