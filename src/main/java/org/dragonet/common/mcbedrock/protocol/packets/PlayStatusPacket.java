package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class PlayStatusPacket extends PEPacket {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED_CLIENT = 1;
    public static final int LOGIN_FAILED_SERVER = 2;
    public static final int PLAYER_SPAWN = 3;
    public static final int LOGIN_FAILED_INVALID_TENANT = 4;
    public static final int LOGIN_FAILED_VANILLA_EDU = 5;
    public static final int LOGIN_FAILED_EDU_VANILLA = 6;

    public int status;
    public int protocol = ProtocolInfo.CURRENT_PROTOCOL;

    public PlayStatusPacket() {

    }

    public PlayStatusPacket(int status) {
        this.status = status;
    }

    public PlayStatusPacket(int status, int protocol) {
        this.status = status;
        this.protocol = protocol;
    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAY_STATUS_PACKET;
    }

    @Override
    public void encodeHeader() {
        if (protocol < 130) { // MCPE <= 1.1
            putByte((byte) (pid() & 0xFF));
        } else {
            super.encodeHeader();
        }
    }

    @Override
    public void encodePayload() {
        putInt(status);
    }

    @Override
    public void decodePayload() {
        status = getInt();
    }
}
