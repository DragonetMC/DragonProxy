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
package org.dragonet.proxy.network.session.cache.object;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.HangingDirection;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPaintingPacket;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;

@Getter
@Setter
public class CachedPainting extends CachedEntity {
    private final String name;
    private HangingDirection hangingDirection;

    public CachedPainting(long proxyEid, int remoteEid, String name) {
        super(EntityType.PAINTING, proxyEid, remoteEid);
        this.name = name;
    }

    @Override
    public void spawn(ProxySession session) {
        AddPaintingPacket addPaintingPacket = new AddPaintingPacket();
        addPaintingPacket.setRuntimeEntityId(proxyEid);
        addPaintingPacket.setUniqueEntityId(proxyEid);
        addPaintingPacket.setPosition(position);
        addPaintingPacket.setDirection(0);
        addPaintingPacket.setName(name);

        session.sendPacket(addPaintingPacket);
        spawned = true;
    }
}
