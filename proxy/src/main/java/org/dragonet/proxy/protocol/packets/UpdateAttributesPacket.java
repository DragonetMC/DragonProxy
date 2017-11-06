package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.entity.PEEntityAttribute;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/23.
 */
public class UpdateAttributesPacket extends PEPacket {

    public long rtid;
    public PEEntityAttribute[] entries;

    @Override
    public int pid() {
        return ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        if(entries != null && entries.length > 0) {
            putUnsignedVarInt(entries.length);
            for(PEEntityAttribute attr : entries) {
                putLFloat(attr.min);
                putLFloat(attr.max);
                putLFloat(attr.currentValue);
                putLFloat(attr.defaultValue);
                putString(attr.name);
            }
        } else {
            putUnsignedVarInt(0);
        }
    }

    @Override
    public void decodePayload() {

    }
}
