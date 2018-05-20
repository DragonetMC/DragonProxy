package org.dragonet.protocol.packets;

import org.dragonet.common.data.blocks.GlobalBlockPalette;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class UpdateBlockPacket extends PEPacket {

    public static final int FLAG_NONE = 0b0000;
    public static final int FLAG_NEIGHBORS = 0b0001;
    public static final int FLAG_NETWORK = 0b0010;
    public static final int FLAG_NOGRAPHIC = 0b0100;
    public static final int FLAG_PRIORITY = 0b1000;

    public static final int FLAG_ALL = FLAG_NEIGHBORS | FLAG_NETWORK;
    public static final int FLAG_ALL_PRIORITY = FLAG_ALL | FLAG_PRIORITY;

    public BlockPosition blockPosition;
    public int id;
    public int data;
    public int flags;
    public int runtimeId;
    public int dataLayer;

    public UpdateBlockPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.UPDATE_BLOCK_PACKET;
    }

    @Override
    public void decodePayload() {
        blockPosition = getBlockPosition();
        runtimeId = (int) getUnsignedVarInt();
        flags = (int) getUnsignedVarInt();
        dataLayer = (int) getUnsignedVarInt();
    }

    @Override
    public void encodePayload() {
        runtimeId = GlobalBlockPalette.getOrCreateRuntimeId(id, data);
        putBlockPosition(blockPosition);
        putUnsignedVarInt(runtimeId);
        putUnsignedVarInt(flags);
        putUnsignedVarInt(dataLayer);
    }
}
