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
package org.dragonet.proxy.protocol.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class LevelEventPacket extends PEPacket {

    public static class Events {

        public final static short EVENT_SOUND_CLICK = (short) 1000;
        public final static short EVENT_SOUND_CLICK_FAIL = (short) 1001;
        public final static short EVENT_SOUND_SHOOT = (short) 1002;
        
        public final static short EVENT_SOUND_DOOR = (short) 1003;
        public final static short EVENT_SOUND_DOOR_OPEN = EVENT_SOUND_DOOR;
        public final static short EVENT_SOUND_DOOR_CLOSE = EVENT_SOUND_DOOR;
        
        public final static short EVENT_SOUND_FIZZ = (short) 1004;

        public final static short EVENT_SOUND_GHAST = (short) 1007;
        public final static short EVENT_SOUND_GHAST_SHOOT = (short) 1008;
        public final static short EVENT_SOUND_BLAZE_SHOOT = (short) 1009;

        public final static short EVENT_SOUND_DOOR_BUMP = (short) 1010;
        public final static short EVENT_SOUND_POUND_WOODEN_DOOR = EVENT_SOUND_DOOR_BUMP;
        public final static short EVENT_SOUND_POUND_METAL_DOOR = EVENT_SOUND_DOOR_BUMP;
        public final static short EVENT_SOUND_BREAK_WOODEN_DOOR = (short) 1012;

        public final static short EVENT_SOUND_BAT_FLY = (short) 1015;
        public final static short EVENT_SOUND_ZOMBIE_INFECT = (short) 1016;
        public final static short EVENT_SOUND_ZOMBIE_HEAL = (short) 1017;

        public final static short EVENT_SOUND_ANVIL_BREAK = (short) 1020;
        public final static short EVENT_SOUND_ANVIL_USE = (short) 1021;
        public final static short EVENT_SOUND_ANVIL_LAND = (short) 1022;

        public final static short EVENT_PARTICLE_SHOOT = (short) 2000;
        public final static short EVENT_PARTICLE_DESTROY = (short) 2001;
        public final static short EVENT_PARTICLE_SPLASH = (short) 2002;
        public final static short EVENT_PARTICLE_EYE_DESPAWN = (short) 2003;
        public final static short EVENT_PARTICLE_SPAWN = (short) 2004;

        public final static short EVENT_START_RAIN = (short) 3001;
        public final static short EVENT_START_THUNDER = (short) 3002;
        public final static short EVENT_STOP_RAIN = (short) 3003;
        public final static short EVENT_STOP_THUNDER = (short) 3004;

        public final static short EVENT_SET_DATA = (short) 4000;

        public final static short EVENT_PLAYERS_SLEEPING = (short) 9800;

        public final static short EVENT_ADD_PARTICLE_MASK = (short) 0x4000;
    }

    public final static byte TAME_FAIL = (byte) 6;
    public final static byte TAME_SUCCESS = (byte) 7;
    public final static byte SHAKE_WET = (byte) 8;
    public final static byte USE_ITEM = (byte) 9;
    public final static byte EAT_GRASS_ANIMATION = (byte) 10;
    public final static byte FISH_HOOK_BUBBLE = (byte) 11;
    public final static byte FISH_HOOK_POSITION = (byte) 12;
    public final static byte FISH_HOOK_HOOK = (byte) 13;
    public final static byte FISH_HOOK_TEASE = (byte) 14;
    public final static byte SQUID_INK_CLOUD = (byte) 15;
    public final static byte AMBIENT_SOUND = (byte) 16;
    public final static byte RESPAWN = (byte) 17;

    public short eventID;
    public float x;
    public float y;
    public float z;
    public int data;

    @Override
    public int pid() {
        return PEPacketIDs.LEVEL_EVENT_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeVarInt(eventID);
            writer.writeVector3f(x, y, z);
            writer.writeVarInt(data);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
