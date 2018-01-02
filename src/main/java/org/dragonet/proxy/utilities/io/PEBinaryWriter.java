/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.utilities.io;

import org.dragonet.proxy.utilities.VarInt;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PEBinaryWriter implements Flushable, Closeable {

    private static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("FFFFFFFFFFFFFFFF", 16);
    protected OutputStream os;
    protected boolean endianness;

    public PEBinaryWriter(OutputStream os) {
        this(os, PEBinaryUtils.BIG_ENDIAN);
    }

    public PEBinaryWriter(OutputStream os, boolean endianness) {
        this.os = os;
        this.endianness = endianness;
    }

    public boolean switchEndianness() {
        this.endianness = !this.endianness;
        return this.endianness;
    }

    public boolean getEndianness() {
        return this.endianness;
    }

    public void flush() throws IOException {
        os.flush();
    }

    public void close() throws IOException {
        os.close();
    }

    public void writeVarInt(int value) throws IOException {
        writeUnsignedVarInt(VarInt.encodeZigZag32(value));
    }

    public void writeUnsignedVarInt(long value) throws IOException {
        writeVar(BigInteger.valueOf(value));
    }

    public void writeVarLong(long value) throws IOException {
        writeUnsignedVarLong(BigInteger.valueOf(VarInt.encodeZigZag64(value)));
    }

    public void writeUnsignedVarLong(BigInteger value) throws IOException {
        writeVar(value);
    }

    public void writeVar(BigInteger v) throws IOException {
        v = v.and(UNSIGNED_LONG_MAX_VALUE);
        BigInteger i = BigInteger.valueOf(-128);
        BigInteger BIX7F = BigInteger.valueOf(0x7f);
        BigInteger BIX80 = BigInteger.valueOf(0x80);
        while (!v.and(i).equals(BigInteger.ZERO)) {
            writeByte(v.and(BIX7F).or(BIX80).byteValue());
            v = v.shiftRight(7);
        }

        writeByte(v.byteValue());
    }

    public void writeVector3f(float x, float y, float z) throws IOException {
        // FORCED to use LITTLE-ENDIAN
        if (endianness != PEBinaryUtils.LITTLE_ENDIAN) {
            switchEndianness();
            writeVector3f(x, y, z);
            switchEndianness();
        } else {
            writeFloat(x);
            writeFloat(y);
            writeFloat(z);
        }
    }

    public void writeBlockCoords(int x, int y, int z) throws IOException {
        writeVarInt(x);
        writeUnsignedVarInt(y);
        writeVarInt(z);
    }

    public void writeUUID(UUID uuid) throws IOException {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public void writeString(String string) throws IOException {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        writeUnsignedVarInt(data.length);
        write(data);
    }

    public void writeAddress(InetAddress addr, short port) throws IOException {
        if (addr instanceof Inet4Address) {
            writeByte((byte) 4);
            writeInt((addr.getAddress()[0] << 24) | (addr.getAddress()[1] << 16) | (addr.getAddress()[2] << 8)
                | addr.getAddress()[3]);
            writeShort(port);
        } else {
            // IPv6? Nah, we do this later.
            writeByte((byte) 6);
            writeLong(0L);
        }
    }

    public void writeByte(byte b) throws IOException {
        os.write(b);
    }

    public void writeBoolean(boolean b) throws IOException {
        writeByte(b ? (byte) 0x01 : (byte) 0x00);
    }

    public void writeShort(short s) throws IOException {
        write(s, 2);
    }

    public void writeTriad(int t) throws IOException {
        this.endianness = !this.endianness;
        write(t, 3);
        this.endianness = !this.endianness;
    }

    public void writeInt(int i) throws IOException {
        write(i, 4);
    }

    public void writeLong(long l) throws IOException {
        write(l, 8);
    }

    public void writeFloat(float f) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putFloat(f);
        os.write(bb.array());
    }

    public void writeDouble(double d) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putDouble(d);
        os.write(bb.array());
    }

    public void write(long x, int len) throws IOException {
        os.write(PEBinaryUtils.write(x, len, endianness));
    }

    public void write(byte[] bytes) throws IOException {
        os.write(bytes);
    }

    public void writeNat(int oneByte) throws IOException {
        os.write(oneByte);
    }
}
