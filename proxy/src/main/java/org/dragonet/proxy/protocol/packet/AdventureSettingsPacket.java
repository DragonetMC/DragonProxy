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

public class AdventureSettingsPacket extends PEPacket {

    public boolean worldImmutable;
    public boolean noPvp;
    public boolean noPvm;
    public boolean noMvp;

    public boolean autoJump;
    public boolean allowFlight;
    public boolean noClip;
    public boolean isFlying;

    public int flags = 0;
    public int userPermission;

    @Override
    public int pid() {
        return PEPacketIDs.ADVENTURE_SETTINGS_PACKET;
    }

    @Override
    public void encode() {
        try {
            if (worldImmutable) flags |= 1;
            if (noPvp) flags |= 1 << 1;
            if (noPvm) flags |= 1 << 2;
            if (noMvp) flags |= 1 << 3;

            if (autoJump) flags |= 1 << 5;
            if (allowFlight) flags |= 1 << 6;
            if (noClip) flags |= 1 << 7;
            if (isFlying) flags |= 1 << 9;

            //Use default channel
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeUnsignedVarInt(this.flags);
            writer.writeUnsignedVarInt(this.userPermission);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
