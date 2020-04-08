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
package org.dragonet.proxy.network.session.cache.object;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;

@Getter
@Setter
public class CachedPlayer extends CachedEntity {
    private final GameProfile profile;

    private GameMode gameMode;

    private float flySpeed = 0.05f;
    private int selectedHotbarSlot = 0;

    public CachedPlayer(long proxyEid, int remoteEid, GameProfile profile) {
        super(BedrockEntityType.PLAYER, proxyEid, remoteEid);
        this.profile = profile;
        metadata.put(EntityData.ALWAYS_SHOW_NAMETAG, (byte) 1);
        metadata.getFlags().setFlag(EntityFlag.ALWAYS_SHOW_NAME, true);
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
        addPlayerPacket.getAdventureSettings().setPlayerPermission(PlayerPermission.MEMBER);
        addPlayerPacket.getAdventureSettings().setCommandPermission(CommandPermission.NORMAL);
        addPlayerPacket.setDeviceId("");

        session.sendPacket(addPlayerPacket);
        spawned = true;
    }

    @Override
    public void moveRelative(ProxySession session, Vector3f relPos, Vector3f rotation, boolean onGround, boolean teleported) {
        if (relPos.getX() == 0 && relPos.getY() == 0 && relPos.getZ() == 0 && position.getX() == 0 && position.getY() == 0)
            return;

        this.position = Vector3f.from(position.getX() + relPos.getX(), position.getY() + relPos.getY(), position.getZ() + relPos.getZ());
        this.rotation = rotation;

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setRuntimeEntityId(proxyEid);
        movePlayerPacket.setEntityType(entityType.getType());
        movePlayerPacket.setMode(teleported ? MovePlayerPacket.Mode.TELEPORT : MovePlayerPacket.Mode.NORMAL);
        movePlayerPacket.setOnGround(onGround);
        movePlayerPacket.setPosition(position);
        movePlayerPacket.setRotation(rotation);

        session.sendPacket(movePlayerPacket);
    }

    @Override
    public void moveAbsolute(ProxySession session, Vector3f position, Vector3f rotation, boolean onGround, boolean teleported) {
        if (position.getX() == 0 && position.getY() == 0 && position.getZ() == 0 && rotation.getX() == 0 && rotation.getY() == 0)
            return;

        this.position = position;
        this.rotation = rotation;

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setRuntimeEntityId(proxyEid);
        movePlayerPacket.setEntityType(entityType.getType());
        movePlayerPacket.setMode(teleported ? MovePlayerPacket.Mode.TELEPORT : MovePlayerPacket.Mode.NORMAL);
        movePlayerPacket.setOnGround(onGround);
        movePlayerPacket.setPosition(position);
        movePlayerPacket.setRotation(rotation);

        session.sendPacket(movePlayerPacket);
    }

    @Override
    public void rotate(ProxySession session, Vector3f rotation) {
        this.rotation = rotation;

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setRuntimeEntityId(proxyEid);
        movePlayerPacket.setPosition(position);
        movePlayerPacket.setRotation(rotation);
        movePlayerPacket.setMode(MovePlayerPacket.Mode.ROTATION);

        session.sendPacket(movePlayerPacket);
    }
}
