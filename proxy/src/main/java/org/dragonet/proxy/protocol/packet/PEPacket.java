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

import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.net.packet.BinaryPacket;

public abstract class PEPacket extends BinaryPacket {

    private int length;

    @Getter
    @Setter
    private NetworkChannel channel = NetworkChannel.CHANNEL_NONE;

    public abstract int pid();

    @Override
    public abstract void encode();

    @Override
    public abstract void decode();

    @Getter
    @Setter
    private boolean shouldSendImmidate;

    public final void setLength(int length) {
        this.length = length;
    }

    public final int getLength() {
        return this.length;
    }
}
