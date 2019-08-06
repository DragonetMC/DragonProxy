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
package org.dragonet.proxy.network.session.cache.object;

import com.flowpowered.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;

public class CachedPlayer extends CachedEntity {

    public CachedPlayer(long entityId) {
        super(EntityType.PLAYER, entityId);
    }

    @Override
    public void spawn(ProxySession session) {
        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUuid(session.getAuthData().getIdentity());
        addPlayerPacket.setUsername(session.getAuthData().getDisplayName());
        addPlayerPacket.setRuntimeEntityId(entityId);
        addPlayerPacket.setUniqueEntityId(entityId);
        addPlayerPacket.setPlatformChatId("");
        addPlayerPacket.setPosition(new Vector3f(0, 50, 0));
        addPlayerPacket.setMotion(Vector3f.ZERO);
        addPlayerPacket.setRotation(Vector3f.ZERO);
        addPlayerPacket.setHand(ItemData.AIR);
        addPlayerPacket.setPlayerFlags(0);
        addPlayerPacket.setCommandPermission(0);
        addPlayerPacket.setWorldFlags(0);
        addPlayerPacket.setPlayerPermission(0);
        addPlayerPacket.setCustomFlags(0);
        addPlayerPacket.setDeviceId("");

        session.getBedrockSession().sendPacket(addPlayerPacket);
        spawned = true;
    }
}
