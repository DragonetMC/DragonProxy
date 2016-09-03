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

public class UnknownPacket extends PEPacket {

    private final int packetId;

    public UnknownPacket(int packetId, byte[] buffer) {
        this.packetId = packetId;
        setData(buffer);
    }
    
    @Override
    public int pid() {
        return packetId;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
    }
    
}
