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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;

@Log4j2
@PEPacketTranslator(packetClass = InventoryTransactionPacket.class)
public class PEInventoryTransactionTranslator extends PacketTranslator<InventoryTransactionPacket> {
    public static final PEInventoryTransactionTranslator INSTANCE = new PEInventoryTransactionTranslator();

    @Override
    public void translate(ProxySession session, InventoryTransactionPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
        if(cachedEntity == null) {
            //log.warn("InteractPacket: Cached entity is null");
            return;
        }
        //log.warn(packet.getTransactionType().name() + " - " + packet.getActionType());

        switch(packet.getTransactionType()) {
            case NORMAL:
                // TODO
                break;
            case ITEM_USE:
                // TODO: different action types
                session.sendRemotePacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                break;
            case ITEM_USE_ON_ENTITY:
                InteractAction interactAction = InteractAction.INTERACT;

                if(packet.getActionType() == 1) { // Attack
                    interactAction = InteractAction.ATTACK;
                }

                session.sendRemotePacket(new ClientPlayerInteractEntityPacket(cachedEntity.getRemoteEid(), interactAction));
                break;
            case ITEM_RELEASE:
                switch(packet.getActionType()) {
                    case 0: // Release
                        Position position = new Position((int) packet.getPlayerPosition().getX(), (int) packet.getPlayerPosition().getY(), (int) packet.getPlayerPosition().getZ());
                        session.sendRemotePacket(new ClientPlayerActionPacket(PlayerAction.RELEASE_USE_ITEM, position, BlockFace.DOWN));
                        break;
                    case 1: // Consume

                        break;
                }
                break;
        }
    }
}
