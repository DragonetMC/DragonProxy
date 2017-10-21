package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Versioning;

/**
 * Created on 2017/10/21.
 */
public class PlayStatusPacket extends PEPacket {

    public final static int LOGIN_SUCCESS = 0;
    public final static int LOGIN_FAILED_CLIENT = 1;
    public final static int LOGIN_FAILED_SERVER = 2;
    public final static int PLAYER_SPAWN = 3;
    public final static int LOGIN_FAILED_INVALID_TENANT = 4;
    public final static int LOGIN_FAILED_VANILLA_EDU = 5;
    public final static int LOGIN_FAILED_EDU_VANILLA = 6;

    public int status;
    public int protocol = Versioning.MINECRAFT_PE_PROTOCOL;

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
        if(protocol < 130) { //MCPE <= 1.1
            putByte((byte)(pid() & 0xFF));
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
