package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class SetPlayerGameTypePacket extends PEPacket {

    public int gamemode;

    public SetPlayerGameTypePacket() {

    }

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
