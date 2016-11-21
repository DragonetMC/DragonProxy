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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PEBinaryReader implements Closeable {

    protected InputStream is;
    protected boolean endianness;

    private int totallyRead;

    public PEBinaryReader(InputStream is) {
        this(is, PEBinaryUtils.BIG_ENDIAN);
    }

    public PEBinaryReader(InputStream is, boolean endianness) {
        this.is = is;
        this.endianness = endianness;
    }

    public boolean switchEndianness() {
        this.endianness = !this.endianness;
        return this.endianness;
    }

    public boolean getEndianness() {
        return this.endianness;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    public static class BinaryAddress {

        public byte type;
        public byte[] address;
        public short port;
    }
    public int readVarInt() throws IOException {
        return VarInt.decodeZigZag32(readUnsignedVarInt());
    }

    public int readUnsignedVarInt() throws IOException {
        return readVar(5).intValue();
    }

    public long readVarLong() throws IOException {
        return VarInt.decodeZigZag64(readUnsignedVarLong()).longValue();
    }

    public BigInteger readUnsignedVarLong() throws IOException {
        return readVar(10);
    }


    public BigInteger readVar(int maxLength) throws IOException {
        BigInteger result = BigInteger.ZERO;
        int offset = 0;
        int b;

        do {
            if (offset >= maxLength) {
                throw new IllegalArgumentException("VarInt overflow");
            }

            b = readByte();
            result = result.or(BigInteger.valueOf((b & 0x7f) << (offset * 7)));
            offset++;
        } while ((b & 0x80) > 0);

        return result;
    }

    public float[] readVector3f() throws IOException {
        // FORCED to use LITTLE-ENDIAN
        if(endianness != PEBinaryUtils.LITTLE_ENDIAN) {
            switchEndianness();
            float[] r = readVector3f();
            switchEndianness();
            return r;
        } else {
            float[] r = new float[] {
                    readFloat(),
                    readFloat(),
                    readFloat()
            };
            return r;
        }
    }

    public int[] readBlockCoords() throws IOException {
        return new int[] {
                readVarInt(),
                readUnsignedVarInt(),
                readVarInt()
        };
    }

    public BinaryAddress readAddress() throws IOException {
        BinaryAddress addr = new BinaryAddress();
        addr.type = readByte();
        if ((addr.type & 0xFF) == 4) {
            //IPv4
            addr.address = read(4);
        } else {
            addr.address = read(8);
        }
        addr.port = readShort();
        return addr;
    }

    public UUID readUUID() throws IOException {
        long first = readLong();
        long last = readLong();
        return new UUID(first, last);
    }


    public String readString() throws IOException {
        int len = readVarInt();
        falloc(len);
        return new String(read(len), StandardCharsets.UTF_8);
    }

    public byte readByte() throws IOException {
        falloc(1);
        totallyRead += 1;
        return (byte) is.read();
    }

    public short readShort() throws IOException {
        falloc(2);
        return (short) (readNat(2) & 0xFFFF);
    }

    public int readTriad() throws IOException {
        falloc(3);
        this.endianness = !this.endianness;
        int triad = (int) (readNat(3) & 0xFFFFFF);
        this.endianness = !this.endianness;
        return triad;
    }

    public int readInt() throws IOException {
        falloc(4);
        return (int) (readNat(4) & 0xFFFFFFFF);
    }

    public long readLong() throws IOException {
        falloc(8);
        return readNat(8);
    }

    public float readFloat() throws IOException {
        falloc(4);
        ByteBuffer bb = ByteBuffer.wrap(read(4));
        return bb.getFloat();
    }

    public double readDouble() throws IOException {
        falloc(8);
        ByteBuffer bb = ByteBuffer.wrap(read(8));
        return bb.getDouble();
    }

    public byte[] read(int length) throws IOException {
        falloc(length);
        this.totallyRead += length;
        byte[] buffer = new byte[length];
        is.read(buffer, 0, length);
        return buffer;
    }

    public long readNat(int length) throws IOException {
        falloc(length);
        return PEBinaryUtils.read(read(length), 0, length, endianness);
    }

    protected void falloc(int l) throws IOException {
        int lack = l - is.available();
        if (lack > 0) {
            throw getUEOFException(lack);
        }
    }

    protected IOException getUEOFException(int needed) {
        return new IOException(String.format("Unexpected end of file: %d more bytes expected", needed));
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public int totallyRead() {
        return this.totallyRead;
    }
}
