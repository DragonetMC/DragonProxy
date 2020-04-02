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
package org.dragonet.proxy.network.translator.bedrock.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import com.nukkitx.protocol.bedrock.packet.RespawnPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.window.BedrockWindowType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PEPacketTranslator(packetClass = PlayerActionPacket.class)
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
            case CONTINUE_BREAK:

                break;
            case BLOCK_INTERACT:
                // Open command block window, as its handled client side on Java edition
                // This may be very fragile. For some reason, if i simply retrieve the command block from the identifier,
                // the runtime id is one higher (1050 vs 1049) than the block at the clicked position in the chunk.
                // For this reason i take away 1 from the command block runtime id, and i should probably look into this.
                // The BEDROCK_TEMP variable is a cheap hacky piece of laziness, anyway.
                if(session.getChunkCache().getBlockAt(packet.getBlockPosition()) == BlockTranslator.BEDROCK_TEMP.get("minecraft:command_block") - 1) {
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
            default:
                log.info(TextFormat.GRAY + "(debug) Unhandled player action: " + packet.getAction().name());
                break;
        }
    }
}
