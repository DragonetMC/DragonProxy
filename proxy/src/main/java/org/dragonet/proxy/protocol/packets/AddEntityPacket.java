package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.entity.PEEntityAttribute;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.PEEntityLink;
import org.dragonet.proxy.utilities.Vector3F;

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

    public EntityMetaData meta;

    public PEEntityAttribute[] attributes;
    public PEEntityLink[] links;

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

        if(attributes.length > 0) {
            putUnsignedVarInt(attributes.length);
            for(PEEntityAttribute attr : attributes) {
                putString(attr.name);
                putLFloat(attr.min);
                putLFloat(attr.currentValue);
                putLFloat(attr.max);
            }
        } else {
            putUnsignedVarInt(0);
        }

        if(meta != null) {
            meta.encode();
            put(meta.getBuffer());
        } else {
            putUnsignedVarInt(0);
        }

        if(links != null && links.length > 0) {
            putUnsignedVarInt(links.length);
            for(PEEntityLink l : links) {
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
        eid = getVarLong();
        rtid = getUnsignedVarLong();
        type = (int) getUnsignedVarInt();
        position = getVector3F();
        motion = getVector3F();
        pitch = getLFloat();
        yaw = getLFloat();

        int lenAttr = (int) getUnsignedVarInt();
        attributes = new PEEntityAttribute[lenAttr];
        if(lenAttr > 0) {
            for(int i = 0; i < lenAttr; i++) {
                String name = getString();
                float min = getLFloat();
                float current = getLFloat();
                float max = getLFloat();
                attributes[i] = PEEntityAttribute.findAttribute(name);
                if(attributes[i] != null) {
                    attributes[i].min = min;
                    attributes[i].max = max;
                    attributes[i].currentValue = current;
                }
            }
        }

        //TODO: read meta!!

        int lenLinks = (int) getUnsignedVarInt();
        links = new PEEntityLink[lenLinks];
        if(lenLinks > 0) {
            for(int i = 0; i < lenLinks; i++) {
                links[i] = getEntityLink();
            }
        }
    }
}
