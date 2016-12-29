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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;
import org.spacehq.mc.protocol.data.game.window.ClickItemParam;
import org.spacehq.mc.protocol.data.game.window.WindowAction;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.MobEquipmentPacket;

public class PEPlayerEquipmentPacketTranslator implements PEPacketTranslator<MobEquipmentPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, MobEquipmentPacket packet) {
        if (packet.selectedSlot > 8) {
            return null;
        }
        if(packet.slot == 0x28 || packet.slot == 0 || packet.slot == 255){
            //That thing changed to air
            //TODO
        }
        if (InventoryTranslatorRegister.HOTBAR_CONSTANTS[packet.selectedSlot] == packet.slot) {
            //Just switched selected slot index, no swapping
            ClientPlayerChangeHeldItemPacket pk = new ClientPlayerChangeHeldItemPacket(packet.selectedSlot);
            return new Packet[]{pk};
        }
        CachedWindow playerInv = session.getWindowCache().getPlayerInventory();
        ItemStack tmp = playerInv.slots[36 + packet.selectedSlot];
        boolean hotbarSlotWasEmpty = tmp == null || tmp.getId() == 0;
        boolean invSlotWasEmpty = playerInv.slots[packet.slot] == null || playerInv.slots[packet.slot].getId() == 0;
        playerInv.slots[36 + packet.selectedSlot] = playerInv.slots[packet.slot];
        playerInv.slots[packet.slot] = tmp;
        session.sendAllPackets(InventoryTranslatorRegister.sendPlayerInventory(session), true);

        //Now the tricky part
        if(!invSlotWasEmpty){
            ClientWindowActionPacket act1 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.slot, playerInv.slots[36 + packet.selectedSlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            ClientWindowActionPacket act2 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), 36 + packet.selectedSlot, tmp, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            if(!hotbarSlotWasEmpty){
                //We have another piece now
                ClientWindowActionPacket act3 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.slot, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
                return new Packet[]{act1, act2, act3};
            }
            return new Packet[]{act1, act2};
        }else{
            ClientWindowActionPacket act1 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), 36 + packet.selectedSlot, playerInv.slots[36 + packet.selectedSlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            ClientWindowActionPacket act2 = new ClientWindowActionPacket(0, (short)(System.currentTimeMillis() & 0xFFFF), packet.slot, null, WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
            return new Packet[]{act1, act2};
        }
    }

}
