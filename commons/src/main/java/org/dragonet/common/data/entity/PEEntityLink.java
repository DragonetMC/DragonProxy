package org.dragonet.common.data.entity;

/**
 * Created on 2017/10/21.
 */
public class PEEntityLink {

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDE = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long riding;
    public long rider;
    public byte type;
    public byte unknownByte = 0x00;

    public PEEntityLink(long riding, long rider, byte type, byte unknownByte) {
        this.riding = riding;
        this.rider = rider;
        this.type = type;
        this.unknownByte = unknownByte;
    }
}
