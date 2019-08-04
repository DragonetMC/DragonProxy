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

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PEMovePlayerTranslator implements PacketTranslator<MovePlayerPacket> {
    public static final PEMovePlayerTranslator INSTANCE = new PEMovePlayerTranslator();

    @Override
    public void translate(ProxySession session, MovePlayerPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getById(packet.getRuntimeEntityId());
        if(cachedEntity == null) {
            log.warn("Cached entity is null in MovePlayerTranslator");
            return;
        }
        log.warn("Cached entity is NOT null");

        ClientPlayerPositionPacket playerPositionPacket = new ClientPlayerPositionPacket(
            packet.isOnGround(),
            packet.getPosition().getX(),
            packet.getPosition().getY(),
            packet.getPosition().getZ()
        );

        session.getDownstream().getSession().send(playerPositionPacket);
    }
}
