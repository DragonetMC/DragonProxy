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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PEPlayerActionTranslator implements PacketTranslator<PlayerActionPacket> {
    public static final PEPlayerActionTranslator INSTANCE = new PEPlayerActionTranslator();

    @Override
    public void translate(ProxySession session, PlayerActionPacket packet) {
        switch(packet.getAction()) {
            case RESPAWN:
                session.getDownstream().getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));

                RespawnPacket respawnPacket = new RespawnPacket();
                respawnPacket.setPosition(session.getCachedEntity().getPosition());
                session.getBedrockSession().sendPacket(respawnPacket);
                break;
            case START_SNEAK:
                ClientPlayerStatePacket startSneakPacket = new ClientPlayerStatePacket((int) packet.getRuntimeEntityId(), PlayerState.START_SNEAKING);
                session.getDownstream().getSession().send(startSneakPacket);
                break;
            case STOP_SNEAK:
                ClientPlayerStatePacket stopSneakPacket = new ClientPlayerStatePacket((int) packet.getRuntimeEntityId(), PlayerState.STOP_SNEAKING);
                session.getDownstream().getSession().send(stopSneakPacket);
                break;
            case START_SPRINT:
                ClientPlayerStatePacket startSprintPacket = new ClientPlayerStatePacket((int) packet.getRuntimeEntityId(), PlayerState.START_SPRINTING);
                session.getDownstream().getSession().send(startSprintPacket);
                break;
            case STOP_SPRINT:
                ClientPlayerStatePacket stopSprintPacket = new ClientPlayerStatePacket((int) packet.getRuntimeEntityId(), PlayerState.STOP_SPRINTING);
                session.getDownstream().getSession().send(stopSprintPacket);
                break;
            case STOP_SLEEP:
                ClientPlayerStatePacket leaveBedPacket = new ClientPlayerStatePacket((int) packet.getRuntimeEntityId(), PlayerState.LEAVE_BED);
                session.getDownstream().getSession().send(leaveBedPacket);
                break;
        }
    }
}
