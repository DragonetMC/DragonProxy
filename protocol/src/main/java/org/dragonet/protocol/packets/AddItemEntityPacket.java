package org.dragonet.protocol.packets;

import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.inventory.Slot;

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
