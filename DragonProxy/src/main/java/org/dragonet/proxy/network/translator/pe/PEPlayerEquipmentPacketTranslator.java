/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;
import org.spacehq.mc.protocol.data.game.window.ClickItemParam;
import org.spacehq.mc.protocol.data.game.window.WindowAction;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.packetlib.packet.Packet;

import sul.protocol.pocket100.play.MobEquipment;

public class PEPlayerEquipmentPacketTranslator implements PEPacketTranslator<MobEquipment> {

    @Override
    public Packet[] translate(ClientConnection session, MobEquipment packet) {
        if (packet.inventorySlot > 8) {
            return new Packet[0];
        }
        if(packet.hotbarSlot == 0x28 || packet.hotbarSlot == 0 || packet.hotbarSlot == 255){
            //That thing changed to air
            //TODO
        }
        if (InventoryTranslatorRegister.HOTBAR_CONSTANTS[packet.inventorySlot] == packet.hotbarSlot) {
            //Just switched selected slot index, no swapping
            ClientPlayerChangeHeldItemPacket pk = new ClientPlayerChangeHeldItemPacket(packet.inventorySlot);
            return new Packet[]{pk};
        }
        CachedWindow playerInv = session.getWindowCache().getPlayerInventory();
        ItemStack tmp = playerInv.slots[36 + packet.inventorySlot];
        boolean hotbarSlotWasEmpty = tmp == null || tmp.getId() == 0;
        boolean invSlotWasEmpty = playerInv.slots[packet.hotbarSlot] == null || playerInv.slots[packet.hotbarSlot].getId() == 0;
        playerInv.slots[36 + packet.inventorySlot] = playerInv.slots[packet.hotbarSlot];
        playerInv.slots[packet.hotbarSlot] = tmp;
        session.sendAllPackets(InventoryTranslatorRegister.sendPlayerInventory(session), true);

        //Now the tricky part
        if(!invSlotWasEmpty){
            ClientWindowActionPacket act1 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.hotbarSlot, playerInv.slots[36 + packet.inventorySlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            ClientWindowActionPacket act2 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), 36 + packet.inventorySlot, tmp, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            if(!hotbarSlotWasEmpty){
                //We have another piece now
                ClientWindowActionPacket act3 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.hotbarSlot, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
                return new Packet[]{act1, act2, act3};
            }
            return new Packet[]{act1, act2};
        }else{
            ClientWindowActionPacket act1 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), 36 + packet.inventorySlot, playerInv.slots[36 + packet.inventorySlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            ClientWindowActionPacket act2 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.hotbarSlot, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            return new Packet[]{act1, act2};
        }
    }

}
