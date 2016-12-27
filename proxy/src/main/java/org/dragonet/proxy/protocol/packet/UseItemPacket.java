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
            int[] bpos = reader.readBlockCoords();
            x = bpos[0]; y = bpos[1]; z = bpos[2];
            this.face = reader.readVarInt();
            float[] fv = reader.readVector3f();
            fx = fv[0]; fy = fv[1]; fz = fv[2];
            float[] fpos = reader.readVector3f();
            posX = fpos[0]; posY = fpos[1]; posZ = fpos[2];
            this.slot = reader.readByte() & 0xFF; // NOT 100% sure for this field
            this.item = PEInventorySlot.readSlot(reader);
        } catch (IOException e) {
        }
    }

}
