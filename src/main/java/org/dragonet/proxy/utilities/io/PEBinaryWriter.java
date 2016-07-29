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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PEBinaryWriter implements Flushable, Closeable {

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

    @Override
    public void flush() throws IOException {
        os.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
    }

    public void writeUUID(UUID uuid) throws IOException {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public void writeString(String string) throws IOException {
        writeString(string, 2);
    }

    public void writeAddress(InetAddress addr, short port) throws IOException {
        if (addr instanceof Inet4Address) {
            writeByte((byte) 4);
            writeInt((addr.getAddress()[0] << 24) | (addr.getAddress()[1] << 16) | (addr.getAddress()[2] << 8) | addr.getAddress()[3]);
            writeShort(port);
        } else {
            //IPv6? Nah, we do this later. 
            writeByte((byte) 6);
            writeLong(0L);
        }
    }

    public void writeString(String string, int lenPrefix) throws IOException {
        byte[] bin = string.getBytes(StandardCharsets.UTF_8);
        write(bin.length, lenPrefix);
        os.write(bin);
    }

    public void writeByte(byte b) throws IOException {
        os.write(b);
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

    public <T> void writeObject(T obj, Object[] args) throws IOException {
        if (obj instanceof CharSequence) {
            boolean written = false;
            if (args.length > 0) {
                if (args[0] instanceof Integer) {
                    writeString(obj.toString(), (int) args[0]);
                    written = true;
                }
            }
            if (!written) {
                writeString(obj.toString());
            }
        } else if (obj instanceof Byte) {
            writeByte((byte) (Byte) obj);
        } else if (obj instanceof Short) {
            writeShort((short) (Short) obj);
        } else if (obj instanceof Integer) {
            writeInt((int) (Integer) obj);
        } else if (obj instanceof Long) {
            writeLong((long) (Long) obj);
        } else if (obj instanceof Float) {
            writeFloat((float) (Float) obj);
        } else if (obj instanceof Double) {
            writeDouble((double) (Double) obj);
        } else {
            writeUnknownType(obj, args);
        }
    }

    protected <T> void writeUnknownType(T obj, Object[] args) throws IOException {
        throw new UnsupportedOperationException(String.format("Unknown object type %s", obj.getClass().getName()));
    }
}
