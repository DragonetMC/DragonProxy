package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

public class AnimatePacket extends PEPacket {

    public static final int ANIMATION_SWING_ARM = 1;
    public static final int ANIMATION_LEAVE_BED = 3;
    public static final int ACTION_CRITICAL_HIT = 4;

    public int action;
    public long eid;
    public float unknown;

    public AnimatePacket() {

    }

    public int pid() {
        return ProtocolInfo.ANIMATE_PACKET;
    }

    @Override
    public void decodePayload() {
        this.action = this.getVarInt();
        this.eid = getEntityRuntimeId();
        if ((this.action & 0x80) != 0) {
            this.unknown = this.getLFloat();
        }
    }

    @Override
    public void encodePayload() {
        this.putVarInt(this.action);
        this.putEntityRuntimeId(this.eid);
        if ((this.action & 0x80) != 0) {
            this.putLFloat(this.unknown);
        }
    }
}
