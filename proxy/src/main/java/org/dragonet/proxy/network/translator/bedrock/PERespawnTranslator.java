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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = RespawnPacket.class)
public class PERespawnTranslator extends PacketTranslator<RespawnPacket> {

    @Override
    public void translate(ProxySession session, RespawnPacket packet) {
        if(packet.getState() == RespawnPacket.State.CLIENT_READY) {
            RespawnPacket respawnPacket = new RespawnPacket();
            respawnPacket.setRuntimeEntityId(packet.getRuntimeEntityId());
            respawnPacket.setPosition(packet.getPosition());
            respawnPacket.setState(RespawnPacket.State.SERVER_SEARCHING);

            session.sendPacket(respawnPacket);

            session.sendRemotePacket(new ClientRequestPacket(ClientRequest.RESPAWN));
        }
    }
}
