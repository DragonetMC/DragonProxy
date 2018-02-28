package org.dragonet.protocol.packets;

import java.util.Collection;

import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.entity.PEEntityLink;

/**
 * Created on 2017/10/21.
 */
public class AddEntityPacket extends PEPacket {

    public long eid;
    public long rtid;
    public int type;
    public Vector3F position;
    public Vector3F motion;
    public float pitch;
    public float yaw;
    public Collection<PEEntityAttribute> attributes;
    public EntityMetaData meta;
    public PEEntityLink[] links;

    public AddEntityPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.ADD_ENTITY_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarLong(eid);
        putUnsignedVarLong(rtid);
        putUnsignedVarInt(type);
        putVector3F(position);
        putVector3F(motion);
        putLFloat(pitch);
        putLFloat(yaw);

        if (attributes != null && attributes.size() > 0) {
            putUnsignedVarInt(attributes.size());
            for (PEEntityAttribute attr : attributes) {
                putString(attr.name);
                putLFloat(attr.min);
                putLFloat(attr.currentValue);
                putLFloat(attr.max);
            }
        } else {
            putUnsignedVarInt(0);
        }

        if (meta != null) {
            meta.encode();
            put(meta.getBuffer());
        } else {
            putUnsignedVarInt(0);
        }

        if (links != null && links.length > 0) {
            putUnsignedVarInt(links.length);
            for (PEEntityLink l : links) {
                putEntityLink(l);
            }
        } else {
            putUnsignedVarInt(0);
        }
    }

    /**
     * decode will NOT work since meta decoding is not implemented
     */
    @Override
    public void decodePayload() {
//        eid = getVarLong();
//        rtid = getUnsignedVarLong();
//        type = (int) getUnsignedVarInt();
//        position = getVector3F();
//        motion = getVector3F();
//        pitch = getLFloat();
//        yaw = getLFloat();
//
//        int lenAttr = (int) getUnsignedVarInt();

//        attributes = new PEEntityAttribute[lenAttr];
//        if (lenAttr > 0) {
//            for (int i = 0; i < lenAttr; i++) {
//                String name = getString();
//                float min = getLFloat();
//                float current = getLFloat();
//                float max = getLFloat();
//                attributes[i] = PEEntityAttribute.findAttribute(name);
//                if (attributes[i] != null) {
//                    attributes[i].min = min;
//                    attributes[i].max = max;
//                    attributes[i].currentValue = current;
//                }
//            }
//        }

        // TODO: read meta!!
//        int lenLinks = (int) getUnsignedVarInt();
//        links = new PEEntityLink[lenLinks];
//        if (lenLinks > 0) {
//            for (int i = 0; i < lenLinks; i++) {
//                links[i] = getEntityLink();
//            }
//        }
    }
}
