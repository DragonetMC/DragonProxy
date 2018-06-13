package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/11/15.
 */
public class CommandRequestPacket extends PEPacket {

    public String command;

    public CommandRequestPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.COMMAND_REQUEST_PACKET;
    }

    @Override
    public void encodePayload() {
        putString(command);
    }

    @Override
    public void decodePayload() {
        command = getString();
    }
}
