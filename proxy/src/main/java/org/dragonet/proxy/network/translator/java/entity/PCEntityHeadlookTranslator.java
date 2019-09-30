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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityHeadlookTranslator implements PacketTranslator<ServerEntityHeadLookPacket> {
    public static final PCEntityHeadlookTranslator INSTANCE = new PCEntityHeadlookTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityHeadLookPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            //log.info("(debug) EntityHeadLook: Cached entity is null");
            return;
        }

        cachedEntity.setRotation(new Vector3f(cachedEntity.getRotation().getX(), cachedEntity.getRotation().getY(), packet.getHeadYaw()));

        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(cachedEntity.getProxyEid());
        moveEntityPacket.setPosition(cachedEntity.getPosition());
        moveEntityPacket.setRotation(cachedEntity.getRotation());
        moveEntityPacket.setOnGround(true);
        moveEntityPacket.setTeleported(false);

        session.sendPacket(moveEntityPacket);
    }
}
