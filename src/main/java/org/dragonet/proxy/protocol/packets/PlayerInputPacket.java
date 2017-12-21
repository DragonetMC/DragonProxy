package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

public class PlayerInputPacket extends PEPacket {

    public float motionX;
    public float motionY;

    public boolean unknownBool1;
    public boolean unknownBool2;

    @Override
    public void decodePayload() {
        this.motionX = this.getLFloat();
        this.motionY = this.getLFloat();
        this.unknownBool1 = this.getBoolean();
        this.unknownBool2 = this.getBoolean();
    }

    @Override
    public void encodePayload() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }

}
