package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;
import org.dragonet.common.mcbedrock.utilities.BlockPosition;

import java.nio.ByteOrder;

import org.dragonet.common.mcbedrock.data.nbt.NBTIO;
import org.dragonet.common.mcbedrock.data.nbt.tag.CompoundTag;

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
