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

public class SetEntityMotionPacket extends PEPacket {

    public EntityMotionData[] motions;

    @Override
    public int pid() {
        return PEPacketIDs.SET_ENTITY_MOTION_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(this.motions.length);
            for (EntityMotionData d : this.motions) {
                if (d == null) {
                    continue;
                }
                writer.writeLong(d.eid);
                writer.writeFloat(d.motionX);
                writer.writeFloat(d.motionY);
                writer.writeFloat(d.motionZ);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

    public static class EntityMotionData {

        public long eid;
        public float motionX;
        public float motionY;
        public float motionZ;
    }
}
