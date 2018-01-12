package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.maths.Vector3F;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class SetEntityMotionPacket extends PEPacket {

    public long rtid;
    public Vector3F motion;

    public SetEntityMotionPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_MOTION_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        putVector3F(motion);
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        motion = getVector3F();
    }
}
