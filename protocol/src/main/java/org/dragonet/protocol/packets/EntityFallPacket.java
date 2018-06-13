/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 *
 * @author Epic
 */
public class EntityFallPacket extends PEPacket {

    public long rtid;
    public float fallDistance;
    public boolean unk1;

    public EntityFallPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.ENTITY_FALL_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        putFloat(fallDistance);
        putBoolean(unk1);
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        fallDistance = getFloat();
        unk1 = getBoolean();
    }
}
