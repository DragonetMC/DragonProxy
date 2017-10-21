package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class SetPlayerGameTypePacket extends PEPacket {

    public int gamemode;

    @Override
    public int pid() {
        return ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(gamemode);
    }

    @Override
    public void decodePayload() {
        gamemode = getVarInt();
    }
}
