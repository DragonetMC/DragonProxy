package org.dragonet.protocol.packets;

import java.util.Collection;

import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/23.
 */
public class UpdateAttributesPacket extends PEPacket {

    public long rtid;
    public Collection<PEEntityAttribute> entries;

    public UpdateAttributesPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        if (entries != null && entries.size() > 0) {
            putUnsignedVarInt(entries.size());
            for (PEEntityAttribute attr : entries) {
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
