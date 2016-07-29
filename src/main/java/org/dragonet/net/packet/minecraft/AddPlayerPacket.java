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
import java.util.UUID;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class AddPlayerPacket extends PEPacket {

    public UUID uuid;
    public String username;
    public long eid;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float yaw;
    public float pitch;
    public PEInventorySlot item;
    public EntityMetaData metadata;

    @Override
    public int pid() {
        return PEPacketIDs.ADD_PLAYER_PACKET;
    }

    @Override
    public void encode() {
        setShouldSendImmidate(true);
        setChannel(NetworkChannel.CHANNEL_ENTITY_SPAWNING);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PEBinaryWriter writer = new PEBinaryWriter(bos);
        try {
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeUUID(uuid);
            writer.writeString(this.username);
            writer.writeLong(this.eid);
            writer.writeFloat(this.x);
            writer.writeFloat(this.y);
            writer.writeFloat(this.z);
            writer.writeFloat(this.speedX);
            writer.writeFloat(this.speedY);
            writer.writeFloat(this.speedZ);
            writer.writeFloat(this.yaw);
            writer.writeFloat(this.yaw); //Head rotation
            writer.writeFloat(this.pitch);
            PEInventorySlot.writeSlot(writer, this.item);
            writer.writeByte((byte) 0x00); // TODO Fix metadata, one of the reasons why skins didn't work properly!
            //writer.write(this.metadata.encode());
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }
}
