/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.nukkitx.protocol.bedrock.packet.AdventureSettingsPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import com.nukkitx.protocol.bedrock.packet.SetHealthPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;


@PCPacketTranslator(packetClass = ServerPlayerHealthPacket.class)
public class PCPlayerHealthTranslator extends PacketTranslator<ServerPlayerHealthPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerHealthPacket packet) {
        SetHealthPacket setHealthPacket = new SetHealthPacket();
        setHealthPacket.setHealth((int) Math.ceil(packet.getHealth()));
        session.sendPacket(setHealthPacket);

        if(packet.getHealth() <= 0) {
            RespawnPacket respawnPacket = new RespawnPacket();
            respawnPacket.setPosition(session.getCachedEntity().getSpawnPosition());
            session.sendPacket(respawnPacket);

            // Tell the server we are ready to respawn
            session.sendRemotePacket(new ClientRequestPacket(ClientRequest.RESPAWN));
        }

        // TODO: update attributes
    }
}
