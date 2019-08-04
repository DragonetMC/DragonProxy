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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityPositionPacketTranslator implements PacketTranslator<ServerEntityPositionPacket> {
    public static final PCEntityPositionPacketTranslator INSTANCE = new PCEntityPositionPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityPositionPacket packet) {
        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();

        moveEntityPacket.setRuntimeEntityId(packet.getEntityId());
        moveEntityPacket.setPosition(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));
        moveEntityPacket.setRotation(new Vector3f(packet.getMovementX(), packet.getMovementY(), packet.getMovementZ()));
        moveEntityPacket.setOnGround(packet.isOnGround());
        moveEntityPacket.setTeleported(false);

        session.getBedrockSession().sendPacket(moveEntityPacket);
    }
}
