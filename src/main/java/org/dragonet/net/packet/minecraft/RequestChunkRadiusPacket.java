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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class RequestChunkRadiusPacket extends PEPacket {

    public int radius;

    @Override
    public int pid() {
        return PEPacketIDs.REQUEST_CHUNK_RADIUS_PACKET;
    }

    @Override
    public void encode() {
        
    }

    @Override
    public void decode() {
        try {
            setChannel(NetworkChannel.CHANNEL_ENTITY_SPAWNING);
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            radius = reader.readInt();
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
