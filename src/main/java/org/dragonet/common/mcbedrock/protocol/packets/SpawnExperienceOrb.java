package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.maths.Vector3F;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

public class SpawnExperienceOrb extends PEPacket {

    public Vector3F position;
    public int count;

    @Override
    public int pid() {
        return ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET;
    }

    @Override
    public void encodePayload() {
        putVector3F(this.position);
        putVarInt(this.count);
    }

    @Override
    public void decodePayload() {
        this.position = getVector3F();
        this.count = getVarInt();
    }

}
