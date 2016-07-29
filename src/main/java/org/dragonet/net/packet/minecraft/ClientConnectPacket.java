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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.proxy.utilities.io.PEBinaryReader;

public class ClientConnectPacket extends PEPacket {

    public long clientID;
    public long sessionID;
    public boolean useSecurity;

    public ClientConnectPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.CLIENT_CONNECT;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.clientID = reader.readLong();
            this.sessionID = reader.readLong();
            this.useSecurity = (reader.readByte() & 0xFF) > 0;
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
