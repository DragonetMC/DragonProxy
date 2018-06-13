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
import org.dragonet.api.translators.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

import java.util.HashSet;
import java.util.Set;

import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.PlayerListPacket;
import org.dragonet.common.data.entity.Skin;

public class PCPlayerListItemPacketTranslator implements IPCPacketTranslator<ServerPlayerListEntryPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerPlayerListEntryPacket packet) {
        PlayerListPacket pk = new PlayerListPacket();
        if (packet.getAction() == PlayerListEntryAction.ADD_PLAYER) {
            PlayerListEntry[] entries = packet.getEntries();
            Set<org.dragonet.protocol.type.PlayerListEntry> peEntries = new HashSet();
            for (PlayerListEntry entry : entries) {
                session.getPlayerInfoCache().put(entry.getProfile().getId(), entry);
                org.dragonet.protocol.type.PlayerListEntry peEntry = new org.dragonet.protocol.type.PlayerListEntry();
                peEntry.uuid = entry.getProfile().getId();
                peEntry.eid = 1;
                peEntry.username = entry.getProfile().getName();
                peEntry.skin = Skin.DEFAULT_SKIN_STEVE;
                peEntry.xboxUserId = entry.getProfile().getId().toString();
                peEntries.add(peEntry);
            }
            pk.type = PlayerListPacket.TYPE_ADD;
            pk.entries = (org.dragonet.protocol.type.PlayerListEntry[]) peEntries.toArray(new org.dragonet.protocol.type.PlayerListEntry[peEntries.size()]);
        } else if (packet.getAction() == PlayerListEntryAction.REMOVE_PLAYER) {
            PlayerListEntry[] entries = packet.getEntries();
            Set<org.dragonet.protocol.type.PlayerListEntry> peEntries = new HashSet();
            for (PlayerListEntry entry : entries) {
                session.getPlayerInfoCache().remove(entry.getProfile().getId());
                org.dragonet.protocol.type.PlayerListEntry peEntry = new org.dragonet.protocol.type.PlayerListEntry();
                peEntry.uuid = entry.getProfile().getId();
                peEntry.eid = 1;
                peEntry.username = entry.getProfile().getName();
                peEntry.skin = Skin.DEFAULT_SKIN_STEVE;
                peEntry.xboxUserId = entry.getProfile().getId().toString();
                peEntries.add(peEntry);
            }
            pk.type = PlayerListPacket.TYPE_REMOVE;
            pk.entries = (org.dragonet.protocol.type.PlayerListEntry[]) peEntries.toArray(new org.dragonet.protocol.type.PlayerListEntry[peEntries.size()]);
        }
        return new PEPacket[]{pk};
    }

}
