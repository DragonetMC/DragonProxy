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
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientRenameItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.ContainerId;
import com.nukkitx.protocol.bedrock.data.InventoryActionData;
import com.nukkitx.protocol.bedrock.data.InventorySource;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedWindow;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.network.translator.misc.inventory.action.DropItemAction;
import org.dragonet.proxy.network.translator.misc.inventory.action.IInventoryAction;
import org.dragonet.proxy.network.translator.misc.inventory.action.SlotChangeAction;
import org.dragonet.proxy.util.TextFormat;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = InventoryTransactionPacket.class)
public class PEInventoryTransactionTranslator extends PacketTranslator<InventoryTransactionPacket> {

    @Override
    public void translate(ProxySession session, InventoryTransactionPacket packet) {
        //log.warn(packet.getTransactionType().name() + " - " + packet.getActionType());

        switch(packet.getTransactionType()) {
            case NORMAL:
                for(InventoryActionData action : packet.getActions()) {
                    InventorySource source = action.getSource();

                    switch(source.getType()) {
                        case WORLD_INTERACTION:
                            executeAction(session, new DropItemAction(action));
                            break;
                        case CREATIVE:
                            switch(action.getSlot()) {
                                case 0: // Delete item
                                    session.sendRemotePacket(new ClientCreativeInventoryActionPacket(-1, ItemTranslatorRegistry.translateToJava(action.getToItem())));
                                    break;
                                case 1: // Create item
                                    session.sendRemotePacket(new ClientCreativeInventoryActionPacket(36 + session.getCachedEntity().getSelectedHotbarSlot(),
                                        ItemTranslatorRegistry.translateToJava(action.getFromItem())));
                                    break;
                            }
                            break;
                        case CONTAINER:
                            CachedWindow inventory = session.getWindowCache().getById(source.getContainerId());
                            if(inventory == null) {
                                log.warn("inventory translator: inventory is null");
                                return;
                            }

                            // TODO
                            executeAction(session, new SlotChangeAction(inventory, action, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                            break;
                        case NON_IMPLEMENTED_TODO:
                            if(source.getContainerId() >= ContainerId.ANVIL_OUTPUT && source.getContainerId() <= -10 /* anvil input */) {
                                if (source.getContainerId() == ContainerId.ANVIL_RESULT) {
                                    ItemData item = action.getFromItem();
                                    String name = item.getTag() != null ? item.getTag().getCompound("display").getString("Name") : "";

                                    session.sendRemotePacket(new ClientRenameItemPacket(name));
                                }
                            }

                            if(source.getContainerId() == ContainerId.BEACON) {
                                CachedWindow beacon = session.getWindowCache().getById(source.getContainerId());
                                if(beacon == null) {
                                    log.warn("inventory translator: beacon inventory is null");
                                    return;
                                }

                                executeAction(session, new SlotChangeAction(beacon, action, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                            }
                            break;
                    }
                }
                break;
            case ITEM_USE:
                switch(packet.getActionType()) {
                    case 1: // Interact block
                    case 3: // Interact air
                        session.sendRemotePacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                        break;
                    case 2: // Break block
                        GameMode gameMode = session.getCachedEntity().getGameMode();
                        session.sendRemotePacket(new ClientPlayerActionPacket(gameMode == GameMode.CREATIVE ? PlayerAction.START_DIGGING : PlayerAction.FINISH_DIGGING,
                            new Position(packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ()), BlockFace.values()[packet.getFace()]));
                        break;
                }
                break;
            case ITEM_USE_ON_ENTITY:
                CachedEntity cachedEntity = session.getEntityCache().getByProxyId(packet.getRuntimeEntityId());
                if(cachedEntity == null) {
                    log.info(TextFormat.GRAY + "(debug) InventoryTransactionPacket: Cached entity is null");
                    return;
                }

                session.setLastClickedEntity(cachedEntity);

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

    private void executeAction(ProxySession session, IInventoryAction action) {
        if(action.isValid(session) && action.execute(session)) {
            action.onSuccess(session);
        } else {
            log.info("inventory action failed: " + action.getClass().getSimpleName());
            action.onFail(session);
        }
    }
}
