package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

public class PlayerInputPacket extends PEPacket {

    public float motionX;
    public float motionY;

    public boolean jumping;
    public boolean sneaking;

    @Override
    public void decodePayload() {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.jumping = this.getBoolean();
        this.sneaking = this.getBoolean();
    }

    @Override
    public void encodePayload() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }

}
