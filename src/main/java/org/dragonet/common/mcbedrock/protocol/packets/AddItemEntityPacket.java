package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.data.entity.meta.EntityMetaData;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;
import org.dragonet.common.mcbedrock.protocol.type.Slot;
import org.dragonet.common.mcbedrock.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class AddItemEntityPacket extends PEPacket {

    public long rtid;
    public long eid;
    public Slot item;
    public Vector3F position;
    public Vector3F motion;
    public EntityMetaData metadata;

    public AddItemEntityPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.ADD_ITEM_ENTITY_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarLong(rtid);
        putUnsignedVarLong(eid);
        putSlot(item);
        putVector3F(position);
        putVector3F(position);
        if (metadata != null) {
            metadata.encode();
            put(metadata.getBuffer());
        } else {
            putUnsignedVarInt(0);
        }
    }

    @Override
    public void decodePayload() {
        rtid = getVarLong();
        eid = getUnsignedVarLong();
        item = getSlot();
        position = getVector3F();
        motion = getVector3F();
        metadata = EntityMetaData.from(this);
    }
}
