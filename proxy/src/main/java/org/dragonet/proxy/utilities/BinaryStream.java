package org.dragonet.proxy.utilities;

import org.dragonet.proxy.nbt.stream.NBTInputStream;
import org.dragonet.proxy.nbt.stream.NBTOutputStream;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.nbt.tag.Tag;
import org.dragonet.proxy.protocol.type.PEEntityLink;
import org.dragonet.proxy.protocol.type.Slot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BinaryStream {

    public int offset;
    private byte[] buffer = new byte[32];
    private int count;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public BinaryStream() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(byte[] buffer) {
        this(buffer, 0);
    }

    public BinaryStream(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
        this.count = buffer.length;
    }

    public void reset() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.count = buffer == null ? -1 : buffer.length;
    }

    public void setBuffer(byte[] buffer, int offset) {
        this.setBuffer(buffer);
        this.setOffset(offset);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte[] getBuffer() {
        return Arrays.copyOf(buffer, count);
    }

    public int getCount() {
        return count;
    }

    public byte[] get() {
        return this.get(this.count - this.offset);
    }

    public byte[] get(int len) {
        if (len < 0) {
            this.offset = this.count - 1;
            return new byte[0];
        }
        len = Math.min(len, this.getCount() - this.offset);
        this.offset += len;
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset);
    }

    public void put(byte[] bytes) {
        if (bytes == null) {
            return;
        }

        this.ensureCapacity(this.count + bytes.length);

        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.length);
        this.count += bytes.length;
    }

    public long getLong() {
        return Binary.readLong(this.get(8));
    }

    public void putLong(long l) {
        this.put(Binary.writeLong(l));
    }

    public int getInt() {
        return Binary.readInt(this.get(4));
    }

    public void putInt(int i) {
        this.put(Binary.writeInt(i));
    }

    public long getLLong() {
        return Binary.readLLong(this.get(8));
    }

    public void putLLong(long l) {
        this.put(Binary.writeLLong(l));
    }

    public int getLInt() {
        return Binary.readLInt(this.get(4));
    }

    public void putLInt(int i) {
        this.put(Binary.writeLInt(i));
    }

    public int getShort() {
        return Binary.readShort(this.get(2));
    }

    public void putShort(int s) {
        this.put(Binary.writeShort(s));
    }

    public int getLShort() {
        return Binary.readLShort(this.get(2));
    }

    public void putLShort(int s) {
        this.put(Binary.writeLShort(s));
    }

    public float getFloat() {
        return getFloat(-1);
    }

    public float getFloat(int accuracy) {
        return Binary.readFloat(this.get(4), accuracy);
    }

    public void putFloat(float v) {
        this.put(Binary.writeFloat(v));
    }

    public float getLFloat() {
        return getLFloat(-1);
    }

    public float getLFloat(int accuracy) {
        return Binary.readLFloat(this.get(4), accuracy);
    }

    public void putLFloat(float v) {
        this.put(Binary.writeLFloat(v));
    }

    public boolean getBoolean() {
        return this.getByte() == 0x01;
    }

    public void putBoolean(boolean bool) {
        this.putByte((byte) (bool ? 1 : 0));
    }

    public int getByte() {
        return this.buffer[this.offset++] & 0xff;
    }

    public void putByte(byte b) {
        this.put(new byte[]{b});
    }

    public void putUUID(UUID uuid) {
        this.put(Binary.writeUUID(uuid));
    }

    public UUID getUUID() {
        return Binary.readUUID(this.get(16));
    }

    public byte[] getByteArray() {
        return this.get((int) this.getUnsignedVarInt());
    }

    public void putByteArray(byte[] b) {
        this.putUnsignedVarInt(b.length);
        this.put(b);
    }

    public String getString() {
        return new String(this.getByteArray(), StandardCharsets.UTF_8);
    }

    public void putString(String string) {
        byte[] b = string.getBytes(StandardCharsets.UTF_8);
        this.putByteArray(b);
    }

    public long getUnsignedVarInt() {
        return VarInt.readUnsignedVarInt(this);
    }

    public void putUnsignedVarInt(long v) {
        VarInt.writeUnsignedVarInt(this, v);
    }

    public int getVarInt() {
        return VarInt.readVarInt(this);
    }

    public void putVarInt(int v) {
        VarInt.writeVarInt(this, v);
    }

    public long getVarLong() {
        return VarInt.readVarLong(this);
    }

    public void putVarLong(long v) {
        VarInt.writeVarLong(this, v);
    }

    public long getUnsignedVarLong() {
        return VarInt.readUnsignedVarLong(this);
    }

    public void putUnsignedVarLong(long v) {
        VarInt.writeUnsignedVarLong(this, v);
    }

    public void putVector3F(float x, float y, float z) {
        this.putLFloat(x);
        this.putLFloat(y);
        this.putLFloat(z);
    }

    public boolean feof() {
        return this.offset < 0 || this.offset >= this.buffer.length;
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buffer.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buffer.length;
        int newCapacity = oldCapacity << 1;

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        this.buffer = Arrays.copyOf(buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }


    /* ==== Methods adapted from PMMP ==== */

    public Vector3F getVector3F() {
        return new Vector3F(getLFloat(), getLFloat(), getLFloat());
    }

    public void putVector3F(Vector3F v) {
        if(v == null) {
            putVector3F(0f, 0f, 0f);
            return;
        }
        putLFloat(v.x);
        putLFloat(v.y);
        putLFloat(v.z);
    }

    public void putSlot(Slot slot) {
        if (slot == null || slot.id == 0) {
            putVarInt(0);
            return;
        }

        putVarInt(slot.id);
        int aux = ((slot.damage & 0x7fff) << 8) | slot.count;
        putVarInt(aux);

        if (slot.tag != null && !slot.tag.isEmpty()) {
            byte[] data = null;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                NBTOutputStream nos = new NBTOutputStream(bos);
                Tag.writeNamedTag(slot.tag, nos);
                nos.close();
                bos.close();
                data = bos.toByteArray();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (data != null && data.length > 0) {
                putLShort(data.length);
                put(data);
            } else {
                putLShort(0);
            }
        } else {
            putLShort(0);
        }

        putVarInt(0);
        putVarInt(0);
    }

    public Slot getSlot() {
        int id = getVarInt();
        if (id == 0) {
            return Slot.AIR.clone();
        }
        int aux = getVarInt();
        int damage = aux >> 8;
        if (damage == 0x7fff) damage = -1;
        int count = aux & 0xff;
        int lNbt = getLShort();
        CompoundTag tag = null;
        if (lNbt > 0) {
            try {
                byte[] bNbt = get(lNbt);
                NBTInputStream bin = new NBTInputStream(new ByteArrayInputStream(bNbt));
                tag = (CompoundTag) Tag.readNamedTag(bin);
                bin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Slot(id, damage, count, tag);
    }

    public BlockPosition getBlockPosition() {
        return new BlockPosition(getVarInt(), (int) getUnsignedVarInt(), getVarInt());
    }

    public void putBlockPosition(BlockPosition blockPosition) {
        putVarInt(blockPosition.x);
        putUnsignedVarInt(blockPosition.y);
        putVarInt(blockPosition.z);
    }

    public BlockPosition getSignedBlockPosition() {
        return new BlockPosition(getVarInt(), getVarInt(), getVarInt());
    }

    public void putSignedBlockPosition(BlockPosition blockPosition) {
        putVarInt(blockPosition.x);
        putVarInt(blockPosition.y);
        putVarInt(blockPosition.z);
    }

    public float getByteRotation(){
        return (((float)(getByte() & 0xFF)) * (360 / 256));
    }

    public void putByteRotation(float rotation){
        putByte((byte) (((int)(rotation / (360 / 256))) & 0xFF));
    }

    public Map<String, GameRule> getGameRules() {
        int count = (int) getUnsignedVarInt();
        Map<String, GameRule> rules = new HashMap<>();
        for(int i = 0; i < count; i++) {
            GameRule rule = GameRule.read(this);
            rules.put(rule.name, rule);
        }
        return rules;
    }

    public void putGameRules(Map<String, GameRule> rules) {
        if(rules == null) {
            putUnsignedVarInt(0);
            return;
        }
        putUnsignedVarInt(rules.size());
        for(GameRule rule : rules.values()) {
            rule.write(this);
        }
    }

    public void putEntityLink(PEEntityLink link) {
        putVarLong(link.eidFrom);
        putVarLong(link.eidTo);
        putByte((byte)(link.type & 0xFF));
        putByte((byte)(link.data & 0xFF));
    }

    public PEEntityLink getEntityLink() {
        PEEntityLink link = new PEEntityLink();
        link.eidFrom = getVarLong();
        link.eidTo = getVarLong();
        link.type = getByte();
        link.data = getByte();
        return link;
    }

}
