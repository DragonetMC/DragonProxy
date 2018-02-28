package org.dragonet.protocol.packets;

import org.dragonet.common.data.entity.PEEntityLink;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 15-10-22.
 */
public class SetEntityLinkPacket extends PEPacket {

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;

//    public PEEntityLink link;
    public long riding;
    public long rider;
    public byte type;
    public byte unknownByte;

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
//        this.putEntityLink(link);
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
