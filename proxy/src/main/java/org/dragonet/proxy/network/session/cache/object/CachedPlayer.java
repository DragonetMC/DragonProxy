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
import com.github.steveice10.mc.auth.data.GameProfile;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import lombok.Getter;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.UUID;

@Getter
public class CachedPlayer extends CachedEntity {
    private GameProfile profile;

    public CachedPlayer(long proxyEid, int remoteEid, GameProfile profile) {
        super(EntityType.PLAYER, proxyEid, remoteEid);
        this.profile = profile;
    }

    @Override
    public void spawn(ProxySession session) {
        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUuid(profile.getId());
        addPlayerPacket.setUsername(profile.getName());
        addPlayerPacket.setRuntimeEntityId(proxyEid);
        addPlayerPacket.setUniqueEntityId(proxyEid);
        addPlayerPacket.setPlatformChatId("");
        addPlayerPacket.setPosition(position);
        addPlayerPacket.setMotion(Vector3f.ZERO);
        addPlayerPacket.setRotation(rotation);
        addPlayerPacket.setHand(ItemData.AIR);
        addPlayerPacket.setPlayerFlags(0);
        addPlayerPacket.setCommandPermission(0);
        addPlayerPacket.setWorldFlags(0);
        addPlayerPacket.setPlayerPermission(0);
        addPlayerPacket.setCustomFlags(0);
        addPlayerPacket.setDeviceId("");

        session.sendPacket(addPlayerPacket);
        spawned = true;

        session.getEntityCache().getEntities().put(proxyEid, this);
    }
}
