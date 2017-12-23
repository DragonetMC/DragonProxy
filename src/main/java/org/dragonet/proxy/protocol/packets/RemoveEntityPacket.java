package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class RemoveEntityPacket extends PEPacket {

    public long eid;

    public RemoveEntityPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.REMOVE_ENTITY_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarLong(eid);
    }

    @Override
    public void decodePayload() {
        eid = getVarLong();
    }
}
