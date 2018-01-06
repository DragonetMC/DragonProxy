package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;

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
