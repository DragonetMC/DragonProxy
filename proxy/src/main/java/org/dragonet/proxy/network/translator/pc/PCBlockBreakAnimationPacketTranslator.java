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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

public class PCBlockBreakAnimationPacketTranslator implements IPCPacketTranslator<ServerBlockBreakAnimPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerBlockBreakAnimPacket packet) {

//        LevelEventPacket pk = new LevelEventPacket();
//        pk.eventId = LevelEventPacket.EVENT_BLOCK_START_BREAK;
//        pk.position = new Vector3F(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
//        
//        // TODO check
//        System.out.println("break : " + packet.getStage().name());
//        pk.data = packet.getStage().ordinal();
//        if(packet.getStage().equals(BlockBreakStage.RESET))
//        {
//            pk.eventId = LevelEventPacket.EVENT_BLOCK_STOP_BREAK;
//        }
        return new PEPacket[]{};
    }
}
