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
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PacketRegisterInfo(packet = PlayerActionPacket.class)
public class PEPlayerActionTranslator extends PacketTranslator<PlayerActionPacket> {

    @Override
    public void translate(ProxySession session, PlayerActionPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
        if(cachedEntity == null) {
            log.info(TextFormat.GRAY + "(debug) Cached entity not found in PEPlayerActionTranslator");
            return;
        }

        switch(packet.getAction()) {
            case RESPAWN:
                // TODO: reset stuff
                break;
            case START_SNEAK:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.START_SNEAKING));
                break;
            case STOP_SNEAK:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.STOP_SNEAKING));
                break;
            case START_SPRINT:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.START_SPRINTING));
                break;
            case STOP_SPRINT:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.STOP_SPRINTING));
                break;
            case STOP_SLEEP:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.LEAVE_BED));
                break;
            case START_GLIDE:
            case STOP_GLIDE:
                session.sendRemotePacket(new ClientPlayerStatePacket(cachedEntity.getRemoteEid(), PlayerState.START_ELYTRA_FLYING));
                break;
            case START_BREAK:
                session.sendRemotePacket(new ClientPlayerActionPacket(PlayerAction.START_DIGGING, new Position(packet.getBlockPosition().getX(),
                    packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()), MagicValues.key(BlockFace.class, packet.getFace())));
                break;
            case ABORT_BREAK:
                session.sendRemotePacket(new ClientPlayerActionPacket(PlayerAction.CANCEL_DIGGING, new Position(packet.getBlockPosition().getX(),
                    packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()), BlockFace.DOWN));
                break;
            case STOP_BREAK:
                session.sendPacket(createLevelEvent(packet.getBlockPosition(), LevelEventType.BLOCK_STOP_BREAK, 0));
                break;
            case CONTINUE_BREAK:
                int runtimeId = session.getChunkCache().getBlockAt(packet.getBlockPosition());
                session.sendPacket(createLevelEvent(packet.getBlockPosition(), LevelEventType.PUNCH_BLOCK, runtimeId | (packet.getFace() << 24)));
                break;
            case BLOCK_INTERACT:
                session.setLastClickedPosition(packet.getBlockPosition());

                // Open command block window, as its handled client side on Java edition
                if(session.getChunkCache().getBlockAt(packet.getBlockPosition()) == BlockTranslator.BEDROCK_COMMAND_BLOCK_ID) {
                    CachedWindow cachedWindow = session.getWindowCache().newWindow(BedrockWindowType.COMMAND_BLOCK, session.getWindowCache().getLocalWindowIdAllocator().getAndIncrement());
                    cachedWindow.setName("Command Block");
                    cachedWindow.open(session);
                    return;
                }

                session.sendRemotePacket(new ClientPlayerPlaceBlockPacket(new Position(packet.getBlockPosition().getX(), packet.getBlockPosition().getY(),
                    packet.getBlockPosition().getZ()), BlockFace.values()[packet.getFace()], Hand.MAIN_HAND, 0, 0, 0, false));
                break;
            case JUMP:
                break;
            case CHANGE_SKIN:
                break;
            case START_SWIMMING:
            case STOP_SWIMMING:
                break;
            default:
                log.info(TextFormat.GRAY + "(debug) Unhandled player action: " + packet.getAction().name());
                break;
        }
    }

    private LevelEventPacket createLevelEvent(Vector3i position, LevelEventType event, int data) {
        LevelEventPacket packet = new LevelEventPacket();
        packet.setType(event);
        packet.setData(data);
        packet.setPosition(position.toFloat());
        return packet;
    }
}
