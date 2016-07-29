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
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class LoginStatusPacket extends PEPacket {

    public final static int LOGIN_SUCCESS = 0;
    public final static int LOGIN_FAILED_CLIENT = 1;
    public final static int LOGIN_FAILED_SERVER = 2;
    public final static int PLAYER_SPAWN = 3;

    public int status;

    @Override
    public int pid() {
        return PEPacketIDs.PLAY_STATUS_PACKET;
    }

    @Override
    public void encode() {
        setShouldSendImmidate(true);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(this.status);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
