package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

/**
 * Created on 2017/10/21.
 */
public class AdventureSettingsPacket extends PEPacket {

    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;

    public static final int LEVEL_PERMISSION_VISITOR = 0;
    public static final int LEVEL_PERMISSION_MEMBER = 1;
    public static final int LEVEL_PERMISSION_OPERATOR = 2;
    public static final int LEVEL_PERMISSION_CUSTOM = 3;

    /**
     * This constant is used to identify flags that should be set on the second
     * field. In a sensible world, these flags would all be set on the same
     * packet field, but as of MCPE 1.2, the new abilities flags have for some
     * reason been assigned a separate field.
     */
    public static final int BITFLAG_SECOND_SET = 1 << 16;

    public static final int WORLD_IMMUTABLE = 0x01;
    public static final int NO_PVP = 0x02;
    public static final int AUTO_JUMP = 0x20;
    public static final int ALLOW_FLIGHT = 0x40;
    public static final int NO_CLIP = 0x80;
    public static final int WORLD_BUILDER = 0x100;
    public static final int FLYING = 0x200;
    public static final int MUTED = 0x400;

    //custom flags (not necessary when using LEVEL_PERMISSION_*)
    public static final int BUILD_AND_MINE = 0x01 | BITFLAG_SECOND_SET;
    public static final int DOORS_AND_SWITCHES = 0x02 | BITFLAG_SECOND_SET;
    public static final int OPEN_CONTAINERS = 0x04 | BITFLAG_SECOND_SET;
    public static final int ATTACK_PLAYERS = 0x08 | BITFLAG_SECOND_SET;
    public static final int ATTACK_MOBS = 0x10 | BITFLAG_SECOND_SET;
    public static final int OPERATOR = 0x20 | BITFLAG_SECOND_SET;
    public static final int TELEPORT = 0x80 | BITFLAG_SECOND_SET;

    public int flags;
    public int commandsPermission;
    public int actionPermissions;
    public int playerPermission;
    public int customFlags;
    public long eid;

    public AdventureSettingsPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.ADVENTURE_SETTINGS_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(flags);
        putUnsignedVarInt(commandsPermission);
        putUnsignedVarInt(actionPermissions);
        putUnsignedVarInt(playerPermission);
        putUnsignedVarInt(customFlags);
        putLLong(eid);
    }

    @Override
    public void decodePayload() {
        flags = (int) getUnsignedVarInt();
        commandsPermission = (int) getUnsignedVarInt();
        actionPermissions = (int) getUnsignedVarInt();
        playerPermission = (int) getUnsignedVarInt();
        customFlags = (int) getUnsignedVarInt();
        eid = getLLong();
    }

    public boolean getFlag(int flag) {
        if ((flag & BITFLAG_SECOND_SET) != 0) {
            return (this.actionPermissions & flag) != 0;
        }

        return (this.flags & flag) != 0;
    }

    public void setFlag(int flag, boolean value) {
        boolean flags = (flag & BITFLAG_SECOND_SET) != 0;

        if (value) {
            if (flags) {
                this.actionPermissions |= flag;
            } else {
                this.flags |= flag;
            }
        } else {
            if (flags) {
                this.actionPermissions &= ~flag;
            } else {
                this.flags &= ~flag;
            }
        }
    }
}
