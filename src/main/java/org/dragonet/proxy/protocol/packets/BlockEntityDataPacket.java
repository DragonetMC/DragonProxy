package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;
import org.dragonet.proxy.data.nbt.NBTIO;
import org.dragonet.proxy.data.nbt.tag.CompoundTag;

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
