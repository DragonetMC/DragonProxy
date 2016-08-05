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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.proxy.utilities.io.PEBinaryReader;

public class UseItemPacket extends PEPacket {

    public int x;
    public int y;
    public int z;
    public int face;
    public PEInventorySlot item;
    public float fx;
    public float fy;
    public float fz;
    public float posX;
    public float posY;
    public float posZ;
    public int slot;

    public UseItemPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.USE_ITEM_PACKET;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.x = reader.readInt();
            this.y = reader.readInt();
            this.z = reader.readInt();
            this.face = reader.readByte() & 0xFF;
            this.fx = reader.readFloat();
            this.fy = reader.readFloat();
            this.fz = reader.readFloat();
            this.posX = reader.readFloat();
            this.posY = reader.readFloat();
            this.posZ = reader.readFloat();
            this.slot = reader.readInt();
            this.item = PEInventorySlot.readSlot(reader);
        } catch (IOException e) {
        }
    }

}
