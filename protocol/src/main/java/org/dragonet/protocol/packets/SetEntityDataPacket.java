package org.dragonet.protocol.packets;

import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/23.
 */
public class SetEntityDataPacket extends PEPacket {

    public long rtid;
    public EntityMetaData meta;

    public SetEntityDataPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_DATA_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        if (meta == null) {
            meta = EntityMetaData.createDefault();
        }
        meta.encode();
        put(meta.getBuffer());
    }

    @Override
    public void decodePayload() {

    }
}
