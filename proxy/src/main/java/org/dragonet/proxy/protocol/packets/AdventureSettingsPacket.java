package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class AdventureSettingsPacket extends PEPacket {

    public final static int PERMISSION_NORMAL = 0;
    public final static int PERMISSION_OPERATOR = 1;
    public final static int PERMISSION_HOST = 2;
    public final static int PERMISSION_AUTOMATION = 3;
    public final static int PERMISSION_ADMIN = 4;

    /**
     * This constant is used to identify flags that should be set on the second field. In a sensible world, these
     * flags would all be set on the same packet field, but as of MCPE 1.2, the new abilities flags have for some
     * reason been assigned a separate field.
     */
    public final static int BITFLAG_SECOND_SET = 1 << 16;

    public final static int WORLD_IMMUTABLE = 0x01;
    public final static int NO_PVP = 0x02;

    public final static int AUTO_JUMP = 0x20;
    public final static int ALLOW_FLIGHT = 0x40;
    public final static int NO_CLIP = 0x80;
    public final static int WORLD_BUILDER = 0x100;
    public final static int FLYING = 0x200;
    public final static int MUTED = 0x400;

    public final static int BUILD_AND_MINE = 0x01 | BITFLAG_SECOND_SET;
    public final static int DOORS_AND_SWITCHES = 0x02 | BITFLAG_SECOND_SET;
    public final static int OPEN_CONTAINERS = 0x04 | BITFLAG_SECOND_SET;
    public final static int ATTACK_PLAYERS = 0x08 | BITFLAG_SECOND_SET;
    public final static int ATTACK_MOBS = 0x10 | BITFLAG_SECOND_SET;
    public final static int OPERATOR = 0x20 | BITFLAG_SECOND_SET;
    public final static int TELEPORT = 0x80 | BITFLAG_SECOND_SET;

    public int flags;
    public int commandsPermission;
    public int flags2;
    public int playerPermission;
    public int customFlags;
    public long eid;


    @Override
    public int pid() {
        return ProtocolInfo.ADVENTURE_SETTINGS_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(flags);
        putUnsignedVarInt(commandsPermission);
        putUnsignedVarInt(flags2);
        putUnsignedVarInt(playerPermission);
        putUnsignedVarInt(customFlags);
        putLLong(eid);
    }

    @Override
    public void decodePayload() {
        flags = (int) getUnsignedVarInt();
        commandsPermission = (int) getUnsignedVarInt();
        flags2 = (int) getUnsignedVarInt();
        playerPermission = (int) getUnsignedVarInt();
        customFlags = (int) getUnsignedVarInt();
        eid = getLLong();
    }


    public boolean getFlag(int flag){
        if((flag & BITFLAG_SECOND_SET) != 0){
            return (this.flags2 & flag) != 0;
        }

        return (this.flags & flag) != 0;
    }

    public void setFlag(int flag, boolean value){
        int flagSet = 0;
        boolean second = false;
        if((flag & BITFLAG_SECOND_SET) != 0){
            flagSet &= this.flags2;
            second = true;
        }else{
            flagSet &= this.flags;
        }

        if(value){
            flagSet |= flag;
        }else{
            flagSet &= ~flag;
        }

        if(second) {
            flags2 = flagSet;
        } else {
            flags = flagSet;
        }
    }
}
