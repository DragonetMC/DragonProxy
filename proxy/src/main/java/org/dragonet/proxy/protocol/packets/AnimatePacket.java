package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

public class AnimatePacket extends PEPacket {
    //vars
    
    public static int ANIMATION_SWING_ARM = 1;
    public static int ACTION_LEFT_CLICK = 2;
    public static int ANIMATION_LEAVE_BED = 3;
    public static int ACTION_MOUSE_HOVER = 4;

    public int action;
    public long eid;
    public float unknown;

    //constructor
    public AnimatePacket() {

    }

    //public
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

    //private
}
