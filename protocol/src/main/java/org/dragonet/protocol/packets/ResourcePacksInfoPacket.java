package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class ResourcePacksInfoPacket extends PEPacket {

    public ResourcePacksInfoPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;
    }

    @Override
    public void encodePayload() {
        // TODO: real encode
        putBoolean(false); //mustAccept
        putLShort(0); //resource pack entries
        putLShort(0); //behavior pack entries
    }

    @Override
    public void decodePayload() {
        // TODO: real decode
    }
}
