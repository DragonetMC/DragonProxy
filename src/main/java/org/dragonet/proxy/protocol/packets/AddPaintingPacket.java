package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

public class AddPaintingPacket extends PEPacket {

    public long eid;
    public long rtid;
    public BlockPosition pos;
    public int direction;
    public String title;

    @Override
    public void decodePayload() {

    }

    @Override
    public void encodePayload() {
        this.reset();
        this.putEntityUniqueId(this.eid);
        this.putEntityRuntimeId(this.rtid);
        this.putBlockPosition(this.pos);
        this.putVarInt(this.direction);
        this.putString(this.title);
    }

    @Override
    public int pid() {
        return ProtocolInfo.ADD_PAINTING_PACKET;
    }

}
