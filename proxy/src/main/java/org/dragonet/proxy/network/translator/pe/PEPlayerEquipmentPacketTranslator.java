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

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.protocol.packets.MobEquipmentPacket;
import org.dragonet.proxy.network.CacheKey;

public class PEPlayerEquipmentPacketTranslator implements IPEPacketTranslator<MobEquipmentPacket> {

    public Packet[] translate(UpstreamSession session, MobEquipmentPacket packet) {
//		System.out.println(org.dragonet.proxy.utilities.DebugTools.getAllFields(packet));
        if (packet.hotbarSlot > 8) {
            return null;
        }
        if (packet.windowId == 0) {
            session.getDataCache().put(CacheKey.PLAYER_SELECTED_SLOT, packet.hotbarSlot);
            return new Packet[]{new ClientPlayerChangeHeldItemPacket(packet.hotbarSlot)};
        }
        return null;
        /*
        if (packet.inventorySlot == 0x28 || packet.inventorySlot == 0 || packet.inventorySlot == 255) {
			// That thing changed to air
			// TODO
		}
		if (InventoryTranslatorRegister.HOTBAR_CONSTANTS[packet.hotbarSlot] == packet.inventorySlot) {
			// Just switched selected slot index, no swapping
			ClientPlayerChangeHeldItemPacket pk = new ClientPlayerChangeHeldItemPacket(packet.hotbarSlot);
			return new Packet[] { pk };
		}
		CachedWindow playerInv = session.getWindowCache().getPlayerInventory();
		ItemStack tmp = playerInv.slots[36 + packet.hotbarSlot];
		boolean hotbarSlotWasEmpty = tmp == null || tmp.getId() == 0;
		boolean invSlotWasEmpty = playerInv.slots[packet.inventorySlot] == null
				|| playerInv.slots[packet.inventorySlot].getId() == 0;
		playerInv.slots[36 + packet.hotbarSlot] = playerInv.slots[packet.inventorySlot];
		playerInv.slots[packet.inventorySlot] = tmp;
		session.sendAllPackets(InventoryTranslatorRegister.sendPlayerInventory(session), true);

		// Now the tricky part
		if (!invSlotWasEmpty) {
			ClientWindowActionPacket act1 = new ClientWindowActionPacket(0,
					(short) (System.currentTimeMillis() & 0xFFFF), packet.inventorySlot,
					playerInv.slots[36 + packet.hotbarSlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
			ClientWindowActionPacket act2 = new ClientWindowActionPacket(0,
					(short) (System.currentTimeMillis() & 0xFFFF), 36 + packet.hotbarSlot, tmp, WindowAction.CLICK_ITEM,
					ClickItemParam.LEFT_CLICK);
			if (!hotbarSlotWasEmpty) {
				// We have another piece now
				ClientWindowActionPacket act3 = new ClientWindowActionPacket(0,
						(short) (System.currentTimeMillis() & 0xFFFF), packet.inventorySlot, null,
						WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
				return new Packet[] { act1, act2, act3 };
			}
			return new Packet[] { act1, act2 };
		} else {
			ClientWindowActionPacket act1 = new ClientWindowActionPacket(0,
					(short) (System.currentTimeMillis() & 0xFFFF), 36 + packet.hotbarSlot,
					playerInv.slots[36 + packet.hotbarSlot], WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);
			ClientWindowActionPacket act2 = new ClientWindowActionPacket(0,
					(short) (System.currentTimeMillis() & 0xFFFF), packet.inventorySlot, null, WindowAction.CLICK_ITEM,
					ClickItemParam.LEFT_CLICK);
			return new Packet[] { act1, act2 };
		}
         */
    }
}
