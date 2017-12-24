package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class MobEffectPacket extends PEPacket {

    public static final byte EVENT_ADD = 1;
    public static final byte EVENT_MODIFY = 2;
    public static final byte EVENT_REMOVE = 3;

    public long rtid;
    public byte eventId;
    public int effectId;
    public int amplifier;
    public boolean particles;
    public int duration;

    public MobEffectPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.MOB_EFFECT_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarLong(rtid);
        putByte(eventId);
        putVarInt(effectId);
        putVarInt(amplifier);
        putBoolean(particles);
        putVarInt(duration);
    }

    @Override
    public void decodePayload() {
        rtid = getUnsignedVarLong();
        eventId = (byte) (getByte() & 0xFF);
        effectId = getVarInt();
        amplifier = getVarInt();
        particles = getBoolean();
        duration = getVarInt();
    }
}
