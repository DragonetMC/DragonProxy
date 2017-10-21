package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class LevelEventPacket extends PEPacket {

    public final static int EVENT_SOUND_CLICK = 1000;
    public final static int EVENT_SOUND_CLICK_FAIL = 1001;
    public final static int EVENT_SOUND_SHOOT = 1002;
    public final static int EVENT_SOUND_DOOR = 1003;
    public final static int EVENT_SOUND_FIZZ = 1004;
    public final static int EVENT_SOUND_IGNITE = 1005;

    public final static int EVENT_SOUND_GHAST = 1007;
    public final static int EVENT_SOUND_GHAST_SHOOT = 1008;
    public final static int EVENT_SOUND_BLAZE_SHOOT = 1009;
    public final static int EVENT_SOUND_DOOR_BUMP = 1010;

    public final static int EVENT_SOUND_DOOR_CRASH = 1012;

    public final static int EVENT_SOUND_ENDERMAN_TELEPORT = 1018;

    public final static int EVENT_SOUND_ANVIL_BREAK = 1020;
    public final static int EVENT_SOUND_ANVIL_USE = 1021;
    public final static int EVENT_SOUND_ANVIL_FALL = 1022;

    public final static int EVENT_SOUND_POP = 1030;

    public final static int EVENT_SOUND_PORTAL = 1032;

    public final static int EVENT_SOUND_ITEMFRAME_ADD_ITEM = 1040;
    public final static int EVENT_SOUND_ITEMFRAME_REMOVE = 1041;
    public final static int EVENT_SOUND_ITEMFRAME_PLACE = 1042;
    public final static int EVENT_SOUND_ITEMFRAME_REMOVE_ITEM = 1043;
    public final static int EVENT_SOUND_ITEMFRAME_ROTATE_ITEM = 1044;

    public final static int EVENT_SOUND_CAMERA = 1050;
    public final static int EVENT_SOUND_ORB = 1051;
    public final static int EVENT_SOUND_TOTEM = 1052;

    public final static int EVENT_SOUND_ARMOR_STAND_BREAK = 1060;
    public final static int EVENT_SOUND_ARMOR_STAND_HIT = 1061;
    public final static int EVENT_SOUND_ARMOR_STAND_FALL = 1062;
    public final static int EVENT_SOUND_ARMOR_STAND_PLACE = 1063;

    //TODO: check 2000-2017
    public final static int EVENT_PARTICLE_SHOOT = 2000;
    public final static int EVENT_PARTICLE_DESTROY = 2001;
    public final static int EVENT_PARTICLE_SPLASH = 2002;
    public final static int EVENT_PARTICLE_EYE_DESPAWN = 2003;
    public final static int EVENT_PARTICLE_SPAWN = 2004;

    public final static int EVENT_GUARDIAN_CURSE = 2006;

    public final static int EVENT_PARTICLE_BLOCK_FORCE_FIELD = 2008;

    public final static int EVENT_PARTICLE_PUNCH_BLOCK = 2014;

    public final static int EVENT_START_RAIN = 3001;
    public final static int EVENT_START_THUNDER = 3002;
    public final static int EVENT_STOP_RAIN = 3003;
    public final static int EVENT_STOP_THUNDER = 3004;
    public final static int EVENT_PAUSE_GAME = 3005; //data: 1 to pause, 0 to resume

    public final static int EVENT_REDSTONE_TRIGGER = 3500;
    public final static int EVENT_CAULDRON_EXPLODE = 3501;
    public final static int EVENT_CAULDRON_DYE_ARMOR = 3502;
    public final static int EVENT_CAULDRON_CLEAN_ARMOR = 3503;
    public final static int EVENT_CAULDRON_FILL_POTION = 3504;
    public final static int EVENT_CAULDRON_TAKE_POTION = 3505;
    public final static int EVENT_CAULDRON_FILL_WATER = 3506;
    public final static int EVENT_CAULDRON_TAKE_WATER = 3507;
    public final static int EVENT_CAULDRON_ADD_DYE = 3508;
    public final static int EVENT_CAULDRON_CLEAN_BANNER = 3509;

    public final static int EVENT_BLOCK_START_BREAK = 3600;
    public final static int EVENT_BLOCK_STOP_BREAK = 3601;

    public final static int EVENT_SET_DATA = 4000;

    public final static int EVENT_PLAYERS_SLEEPING = 9800;

    public final static int EVENT_ADD_PARTICLE_MASK = 0x4000;

    public int eventId;
    public Vector3F position;
    public int data;

    @Override
    public int pid() {
        return ProtocolInfo.LEVEL_EVENT_PACKET;
    }

    @Override
    public void encodePayload() {
        putVarInt(eventId);
        putVector3F(position);
        putVarInt(data);
    }

    @Override
    public void decodePayload() {
        eventId = getVarInt();
        position = getVector3F();
        data = getVarInt();
    }
}
