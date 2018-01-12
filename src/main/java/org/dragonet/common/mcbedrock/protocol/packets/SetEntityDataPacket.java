package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.data.entity.meta.EntityMetaData;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

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
