package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class ResourcePacksInfoPacket extends PEPacket {

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;
    }

    @Override
    public void encodePayload() {
        //TODO: real encode
        putBoolean(false);
        putLShort(0);
        putLShort(0);
    }

    @Override
    public void decodePayload() {
        //TODO: real decode
    }
}
