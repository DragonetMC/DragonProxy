package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.maths.BlockPosition;

import java.nio.ByteOrder;

import org.dragonet.common.data.nbt.NBTIO;
import org.dragonet.common.data.nbt.tag.CompoundTag;

/**
 * Created on 2017/10/22.
 */
public class BlockEntityDataPacket extends PEPacket {

    public BlockPosition blockPosition;
    public CompoundTag tag;

    public BlockEntityDataPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }

    @Override
    public void encodePayload() {
        putBlockPosition(blockPosition);
        if (tag != null) {
            byte[] bytes = new byte[]{};
            try {
                bytes = NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            put(bytes);
        }
    }

    @Override
    public void decodePayload() {
        blockPosition = getBlockPosition();
        try {
            tag = (CompoundTag) NBTIO.read(get(), ByteOrder.LITTLE_ENDIAN, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
