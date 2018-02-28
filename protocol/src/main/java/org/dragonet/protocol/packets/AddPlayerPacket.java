package org.dragonet.protocol.packets;

import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.entity.PEEntityLink;
import org.dragonet.common.data.inventory.Slot;

import java.util.UUID;

/**
 * Created on 2017/10/21.
 * https://github.com/NiclasOlofsson/MiNET/blob/master/src/MiNET/MiNET/Net/MCPE%20Protocol%20Documentation.md#add-player-0x0c
 */
public class AddPlayerPacket extends PEPacket {

    public UUID uuid;
    public String username;
    public String thirdpartyName;
    public int platformID = 0;
    public long eid;
    public long rtid;
    public String platformChatID = "";
    public Vector3F position;
    public Vector3F motion;
    public float pitch;
    public float yaw;
    public float headYaw;
    public Slot item;
    public EntityMetaData meta;
    public PEEntityLink[] links;

    public AddPlayerPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.ADD_PLAYER_PACKET;
    }

    @Override
    public void encodePayload() {
        putUUID(uuid);
        putString(username);
//        putString(thirdpartyName != null ? thirdpartyName : username);
//        putVarInt(platformID);
        putVarLong(eid);
        putUnsignedVarLong(rtid);
//        putString(platformChatID);
        putVector3F(position);
        putVector3F(motion);
        putLFloat(pitch);
        putLFloat(yaw);
        putLFloat(headYaw);
        putSlot(item);
        if (meta != null) {
            meta.encode();
            put(meta.getBuffer());
        } else {
            putUnsignedVarInt(0);
        }
        putUnsignedVarInt(0); //Flags
        putUnsignedVarInt(0); //Command permission
        putUnsignedVarInt(0); //Action Permissions
        putUnsignedVarInt(0); //Permission Level
        putUnsignedVarInt(0); //Custom stored permissions
        putLLong(0L);         //User Id
        if (links != null && links.length > 0) {
            putUnsignedVarInt(links.length);
            for (PEEntityLink l : links) {
                putEntityLink(l);
            }
        } else {
            putUnsignedVarInt(0);
        }
    }

    @Override
    public void decodePayload() {
        uuid = getUUID();
        username = getString();
        eid = getVarLong();
        rtid = getUnsignedVarLong();
        position = getVector3F();
        motion = getVector3F();
        pitch = getLFloat();
        yaw = getLFloat();
        headYaw = getLFloat();
        item = getSlot();
        // meta = getEntityMetadata();
        meta = EntityMetaData.createDefault(); // TODO

//        getUnsignedVarInt();
//        getUnsignedVarInt();
//        getUnsignedVarInt();
//        getUnsignedVarInt();
//        getUnsignedVarInt();
//
//        getLLong();
//
//        int linkCount = (int) getUnsignedVarInt();
//        links = new PEEntityLink[linkCount];
//        if (linkCount > 0) {
//            for (int i = 0; i < linkCount; ++i) {
//                links[i] = getEntityLink();
//            }
//        }
    }
}
