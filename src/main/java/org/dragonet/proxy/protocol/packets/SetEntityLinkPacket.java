package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 15-10-22.
 */
public class SetEntityLinkPacket extends PEPacket {

    public static final byte TYPE_REMOVE = 3;
    public static final byte TYPE_RIDE = 2;
    public static final byte TYPE_PASSENGER = 0;

    public long riding;
    public long rider;
    public byte type;
    public byte unknownByte;

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.putEntityUniqueId(this.riding);
        this.putEntityUniqueId(this.rider);
        this.putByte(this.type);
        this.putByte(this.unknownByte);
    }

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_LINK_PACKET;
    }
}
