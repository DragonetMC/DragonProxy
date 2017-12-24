package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.window.DropItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowActionParam;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.packetlib.packet.Packet;
import java.util.ArrayList;
import java.util.List;

import org.dragonet.proxy.data.inventory.ContainerId;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.protocol.packets.InventoryTransactionPacket;
import static org.dragonet.proxy.protocol.packets.InventoryTransactionPacket.*;
import org.dragonet.proxy.protocol.type.InventoryTransactionAction;
import org.dragonet.proxy.protocol.type.Slot;
import org.dragonet.proxy.protocol.type.transaction.data.ReleaseItemData;
import org.dragonet.proxy.protocol.type.transaction.data.TransactionData;
import org.dragonet.proxy.protocol.type.transaction.data.UseItemData;
import org.dragonet.proxy.protocol.type.transaction.data.UseItemOnEntityData;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.DebugTools;
import org.dragonet.proxy.utilities.NukkitMath;

/**
 * Created on 2017/12/3.
 */
public class PEInventoryTransactionPacketTranslator implements IPEPacketTranslator<InventoryTransactionPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, InventoryTransactionPacket packet) {
        //debug
        System.out.println(">>>>============================");
        System.out.println("InventoryTransactionPacket type: \n" + DebugTools.getAllFields(packet));
        System.out.println("-------------");
        for (InventoryTransactionAction action : packet.actions) {
            System.out.println(DebugTools.getAllFields(action));
        }
        System.out.println("<<<<============================");

        switch (packet.transactionType) {
            case TYPE_NORMAL: //0
                System.out.println("TYPE_NORMAL");
                if (packet.actions.length <= 2 && packet.actions[0].sourceType == InventoryTransactionAction.SOURCE_WORLD) //main inventory
                {
                    // drop item
                    ClientPlayerActionPacket act = new ClientPlayerActionPacket(
                            com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.DROP_ITEM,
                            new Position(0, 0, 0),
                            BlockFace.DOWN);
                    return new Packet[]{act};
                }
                if (packet.actions.length == 2 && packet.actions[0].sourceType == InventoryTransactionAction.SOURCE_CONTAINER && packet.actions[1].containerId == ContainerId.CURSOR.getId()) {
                    // desktop version: click on an item (maybe pick/place/merge/swap)
                }
                // after the previous one, we can detect SHIFT click or move items on mobile devices
                if (packet.actions.length == 2 && packet.actions[0].sourceType == InventoryTransactionAction.SOURCE_CONTAINER && packet.actions[2].sourceType == InventoryTransactionAction.SOURCE_CONTAINER) {
                    // mobile version: move item
                    // desktop version: SHIFT-click item
                }
                return null; // it's okay to return null
            case TYPE_MISMATCH: //1
                System.out.println("TYPE_MISMATCH");
                break;
            case TYPE_USE_ITEM: //2
                System.out.println("TYPE_USE_ITEM");
                UseItemData useItemData = (UseItemData) packet.transactionData;
                if (useItemData.blockPos.equals(new BlockPosition(0, 0, 0))) {
                    return null;
                }
                switch (useItemData.actionType) {
                    case InventoryTransactionPacket.USE_ITEM_ACTION_BREAK_BLOCK: //2
                    {
                        ClientPlayerActionPacket act = new ClientPlayerActionPacket(
                                com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.START_DIGGING,
                                new Position(useItemData.blockPos.x, useItemData.blockPos.y, useItemData.blockPos.z),
                                MagicValues.key(BlockFace.class, useItemData.face));
                        session.getDataCache().put(CacheKey.BLOCK_BREAKING_POSITION, act.getPosition());
                        return new Packet[]{act};
                    }
                    case InventoryTransactionPacket.USE_ITEM_ACTION_CLICK_BLOCK: //0
                    case InventoryTransactionPacket.USE_ITEM_ACTION_CLICK_AIR: //1
                    {
                        ClientPlayerPlaceBlockPacket placePacket = new ClientPlayerPlaceBlockPacket(
                                new Position(useItemData.blockPos.x, useItemData.blockPos.y, useItemData.blockPos.z),
                                ItemBlockTranslator.translateToPC(useItemData.face),
                                Hand.MAIN_HAND,
                                useItemData.clickPos.x,
                                useItemData.clickPos.y,
                                useItemData.clickPos.z
                        );
                        return new Packet[]{placePacket, new ClientPlayerSwingArmPacket(Hand.MAIN_HAND)};
                    }

                }
            case TYPE_USE_ITEM_ON_ENTITY: //3
                System.out.println("TYPE_USE_ITEM_ON_ENTITY");
                UseItemOnEntityData useItemOnEntityData = (UseItemOnEntityData) packet.transactionData;
                CachedEntity cachedEntity = session.getEntityCache().getByLocalEID(useItemOnEntityData.entityRuntimeId);
                if (cachedEntity == null) {
                    return null;
                }
                InteractAction interractAction = InteractAction.INTERACT;
                if (useItemOnEntityData.actionType == InventoryTransactionPacket.USE_ITEM_ON_ENTITY_ACTION_ATTACK) {
                    interractAction = InteractAction.ATTACK;
                }
                ClientPlayerInteractEntityPacket interractPacket = new ClientPlayerInteractEntityPacket(
                        (int) cachedEntity.eid,
                        interractAction
                );
                return new Packet[]{interractPacket};
            case TYPE_RELEASE_ITEM: //4
                System.out.println("TYPE_RELEASE_ITEM");
                ReleaseItemData releaseItemData = (ReleaseItemData) packet.transactionData;
//                ClientPlayerActionPacket act = new ClientPlayerActionPacket(
//                    com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.DROP_ITEM,
//                    new Position(
//                            (int)session.getEntityCache().getClientEntity().x,
//                            (int)session.getEntityCache().getClientEntity().y,
//                            (int)session.getEntityCache().getClientEntity().z
//                    ),
//                    BlockFace.UP);
//                ClientWindowActionPacket windowPacket = new ClientWindowActionPacket(
//                        0,
//                        0, //transaction id
//                        releaseItemData.hotbarSlot,
//                        ItemBlockTranslator.translateToPC(releaseItemData.itemInHand),
//                        WindowAction.DROP_ITEM,
//                        (WindowActionParam)DropItemParam.DROP_FROM_SELECTED
//                );
//                return new Packet[] {act, windowPacket}; 
        }
        return null;
    }

}
