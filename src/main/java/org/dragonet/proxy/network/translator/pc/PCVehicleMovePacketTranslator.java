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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.utilities.DebugTools;

public class PCVehicleMovePacketTranslator implements IPCPacketTranslator<ServerVehicleMovePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerVehicleMovePacket packet) {
//        System.out.println(DebugTools.getAllFields(packet));
        return new PEPacket[]{};
    }
}
