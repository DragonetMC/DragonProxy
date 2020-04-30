/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = SetPlayerGameTypePacket.class)
public class PESetPlayerGameTypeTranslator extends PacketTranslator<SetPlayerGameTypePacket> {

    @Override
    public void translate(ProxySession session, SetPlayerGameTypePacket packet) {
        CachedPlayer player = session.getCachedEntity();

        // Reset the game mode on the client
        SetPlayerGameTypePacket setPlayerGameTypePacket = new SetPlayerGameTypePacket();
        setPlayerGameTypePacket.setGamemode(player.getGameMode().ordinal());
        session.sendPacket(setPlayerGameTypePacket);

        // Tell the server to update our game mode, and then its up to the server whether it happens or not
        session.sendRemotePacket(new ClientChatPacket("/gamemode " + GameMode.values()[packet.getGamemode()].name().toLowerCase()));
    }
}
