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

public class SetHealthPacket extends PEPacket {

    public int health;

    public SetHealthPacket() {
    }

    public SetHealthPacket(int health) {
        this.health = health;
    }

    @Override
    public int pid() {
        return PEPacketIDs.SET_HEALTH_PACKET;
    }

    @Override
    public void encode() {
        setShouldSendImmidate(true);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(this.health);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
