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
package org.dragonet.proxy.network.translator.java.entity;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
public class PCEntityPositionRotationPacketTranslator implements PacketTranslator<ServerEntityPositionRotationPacket> {
    public static final PCEntityPositionRotationPacketTranslator INSTANCE = new PCEntityPositionRotationPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPositionRotationPacket packet) {
        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();

        log.trace("GOT entity position rotation x=" + packet.getMovementX() + ", y=" + packet.getMovementY() + ", z=" + packet.getMovementZ());

        moveEntityPacket.setRuntimeEntityId(packet.getEntityId());
        moveEntityPacket.setPosition(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));
        moveEntityPacket.setTeleported(false);
        moveEntityPacket.setOnGround(true);
        moveEntityPacket.setRotation(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));

        session.getBedrockSession().sendPacket(moveEntityPacket);
    }
}
