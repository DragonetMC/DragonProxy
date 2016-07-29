/**
 * JRakLib is not affiliated with Jenkins Software LLC or RakNet.
 * This software is a port of RakLib https://github.com/PocketMine/RakLib.

 * This file is part of JRakLib.
 *
 * JRakLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JRakLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JRakLib.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.raknet.client;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Binary Utility class for writing/reading.
 */
public class Binary {

    /**
     * Reads a 3-byte little-endian number
     * @param bytes
     * @return integer
     */
    public static int readLTriad(byte[] bytes){
        return (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[2] & 0x0F) << 16);
    }

    /**
     * Writes a 3-byte little-endian number
     * @param triad
     * @return triad bytes
     */
    public static byte[] writeLTriad(int triad){
        byte b1,b2,b3;
        b3 = (byte)(triad & 0xFF);
        b2 = (byte)((triad >> 8) & 0xFF);
        b1 = (byte)((triad >> 16) & 0xFF);
        return new byte[] {b3, b2, b1};
    }

    /**
     * Reads a signed byte as a boolean
     * @param b The raw byte
     * @return The boolean
     */
    public static boolean readBool(byte b){
        if(b == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes a signed boolean as a byte
     * @param b The boolean
     * @return Boolean as a byte
     */
    public static byte writeBool(boolean b){
        if(b){
            return 0x01;
        } else {
            return 0x00;
        }
    }

    /**
     * Reads a signed/unsigned byte
     * @param b The raw byte
     * @param signed If the byte is signed
     * @return Signed/unsigned byte as int.
     */
    public static int readByte(byte b, boolean signed){
        if(signed){
            return b;
        } else {
            return b & 0xFF;
        }
    }

    /**
     * Writes a signed/unsigned byte.
     * @param b Raw byte
     * @return The byte.
     */
    public static byte writeByte(byte b){
        return b;
    }

    /**
     * Reads an unsigned 16 bit big-endian number.
     * @param bytes Raw bytes
     * @return The unsigned short.
     */
    public static int readShort(byte[] bytes){
        return ((bytes[0] << 8) & 0x0000ff00) | (bytes[1] & 0x000000ff);
    }

    /**
     * Reads a signed 16 bit big-endian number.
     * @param bytes Raw bytes
     * @return The signed short.
     */
    public static short readSignedShort(byte[] bytes){
        return ByteBuffer.wrap(bytes).getShort();
    }

    /**
     * Writes a signed 16 bit big-endian number.
     * @param s The short
     * @return Short as a byte array
     */
    public static byte[] writeShort(short s){
        return ByteBuffer.allocate(2).putShort(s).array();
    }

    /**
     * Writes an unsigned 16 bit big-endian number.
     * @param s The unsigned short (integer)
     * @return Short as a byte array
     */
    public static byte[] writeUnsignedShort(int s){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) ((s >> 8) & 0xff));
        bb.put((byte) (s & 0xff));
        return bb.array();
    }

    /**
     * Reads a signed 32 bit big-endian number.
     * @param bytes Raw bytes
     * @return The integer.
     */
    public static int readInt(byte[] bytes){
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * Writes a signed 32 bit big-endian number.
     * @param i The integer.
     * @return Integer as a byte array
     */
    public static byte[] writeInt(int i){
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    /**
     * Reads a signed 32 bit big-endian floating point number.
     * @param bytes Raw bytes
     * @return The float
     */
    public static float readFloat(byte[] bytes){
        return ByteBuffer.wrap(bytes).getFloat();
    }

    /**
     * Writes a signed 32 bit big-endian floating point number.
     * @param f The float.
     * @return The float as a byte array
     */
    public static byte[] writeFloat(float f){
        return ByteBuffer.allocate(4).putFloat(f).array();
    }

    /**
     * Reads a signed 64 bit big-endian double precision number.
     * @param bytes Raw bytes
     * @return The double.
     */
    public static double readDouble(byte[] bytes){
        return ByteBuffer.wrap(bytes).getFloat();
    }

    /**
     * Writes a signed 64 bit big-endian double precision number.
     * @param d The double.
     * @return The double as a byte array
     */
    public static byte[] writeDouble(double d){
        return ByteBuffer.allocate(8).putDouble(d).array();
    }

    /**
     * Reads a signed 64 bit big-endian number.
     * @param bytes Raw bytes
     * @return The long
     */
    public static long readLong(byte[] bytes){
        return ByteBuffer.wrap(bytes).getLong();
    }

    /**
     * Writes a signed 64 bit big-endian number.
     * @param l The long.
     * @return The long as a byte array.
     */
    public static byte[] writeLong(long l){
        return ByteBuffer.allocate(8).putLong(l).array();
    }

    public static byte[] subbytes(byte[] bytes, int start, int length){
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.position(start);
        byte[] bytes2 = new byte[length];
        bb.get(bytes2);
        return bytes2;
    }

    public static byte[] subbytes(byte[] bytes, int start){
        return subbytes(bytes, start, bytes.length - start);
    }

    public static byte[][] splitbytes(byte[] bytes, int chunkSize){
        byte[][] splits = new byte[1024][chunkSize];
        int chunks = 0;
        for(int i=0;i<bytes.length;i+=chunkSize){
            if((bytes.length - i) > chunkSize){
                splits[chunks] = Arrays.copyOfRange(bytes, i, i + chunkSize);
            } else {
                splits[chunks] = Arrays.copyOfRange(bytes, i, bytes.length);
            }
            chunks++;
        }

        splits = Arrays.copyOf(splits, chunks);

        return splits;
    }
}