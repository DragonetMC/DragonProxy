package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.nbt.tag.Tag;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created on 2017/10/22.
 */
public class BlockEntityDataPacket extends PEPacket {

    public BlockPosition blockPosition;
    public CompoundTag tag;

    @Override
    public int pid() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }

    @Override
    public void encodePayload() {
        putBlockPosition(blockPosition);
        if(tag != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            NBTOutputStream nos = new NBTOutputStream(bos);
            try {
                Tag.writeNamedTag(tag, nos);
                nos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            put(bos.toByteArray());
        }
    }

    @Override
    public void decodePayload() {
        blockPosition = getBlockPosition();
        NBTInputStream nin = new NBTInputStream(new ByteArrayInputStream(get(getBuffer().length - offset)));
        try {
            tag = (CompoundTag) Tag.readNamedTag(nin);
            nin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
