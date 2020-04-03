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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.MoveToHotbarParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.InventorySource;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PEPacketTranslator(packetClass = InventoryTransactionPacket.class)
public class PEInventoryTransactionTranslator extends PacketTranslator<InventoryTransactionPacket> {

    @Override
    public void translate(ProxySession session, InventoryTransactionPacket packet) {
        //log.warn(packet.getTransactionType().name() + " - " + packet.getActionType());

        switch(packet.getTransactionType()) {
            case NORMAL:
                for(InventoryActionData action : packet.getActions()) {
                    //log.info("ACTION: " + action.getSource().getType().name());
                    switch(action.getSource().getType()) {
                        case WORLD_INTERACTION:
                            session.sendRemotePacket(new ClientPlayerActionPacket(PlayerAction.DROP_ITEM, new Position(0, 0, 0), BlockFace.UP));
                            break;
                    }
                }
                break;
            case ITEM_USE:
                // TODO: different action types
                session.sendRemotePacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                break;
            case ITEM_USE_ON_ENTITY:
                CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
                if(cachedEntity == null) {
                    log.info(TextFormat.GRAY + "(debug) InventoryTransactionPacket: Cached entity is null");
                    return;
                }

                InteractAction interactAction = InteractAction.values()[packet.getActionType()];
                Vector3f clickPos = packet.getClickPosition();

                session.sendRemotePacket(new ClientPlayerInteractEntityPacket(cachedEntity.getRemoteEid(),
                    interactAction, clickPos.getX(), clickPos.getY(), clickPos.getZ(), Hand.MAIN_HAND));
                break;
            case ITEM_RELEASE:
                switch(packet.getActionType()) {
                    case 0: // Release
                        session.sendRemotePacket(new ClientPlayerActionPacket(PlayerAction.RELEASE_USE_ITEM, new Position(0, 0, 0), BlockFace.DOWN));
                        break;
                    case 1: // Consume

                        break;
                }
                break;
        }
    }
}
