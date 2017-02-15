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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.PlayerListEntryAction;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

import net.marfgamer.jraknet.RakNetPacket;

public class PCPlayerListItemPacketTranslator implements PCPacketTranslator<ServerPlayerListEntryPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerPlayerListEntryPacket packet) {
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
        return new RakNetPacket[0];
    }

}
