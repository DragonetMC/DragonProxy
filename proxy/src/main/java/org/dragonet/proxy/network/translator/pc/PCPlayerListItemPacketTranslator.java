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

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.dragonet.proxy.protocol.PEPacket;

public class PCPlayerListItemPacketTranslator implements PCPacketTranslator<ServerPlayerListEntryPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerListEntryPacket packet) {
        if(packet.getAction() == PlayerListEntryAction.ADD_PLAYER){
            PlayerListEntry[] entries = packet.getEntries();
            for (PlayerListEntry entrie : entries) {
                session.getPlayerInfoCache().put(entrie.getProfile().getId(), entrie);
            }
        }else if(packet.getAction() == PlayerListEntryAction.REMOVE_PLAYER){
            PlayerListEntry[] entries = packet.getEntries();
            for (PlayerListEntry entrie : entries) {
                session.getPlayerInfoCache().remove(entrie.getProfile().getId());
            }
        }
        return null;
    }

}
