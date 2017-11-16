package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.InventoryTransactionAction;

/**
 * Created on 2017/10/21.
 */
public class InventoryTransactionPacket extends PEPacket {
	//vars
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_MISMATCH = 1;
	public static final int TYPE_USE_ITEM = 2;
	public static final int TYPE_USE_ITEM_ON_ENTITY = 3;
	public static final int TYPE_RELEASE_ITEM = 4;
	
	public static final int USE_ITEM_ACTION_CLICK_BLOCK = 0;
	public static final int USE_ITEM_ACTION_CLICK_AIR = 1;
	public static final int USE_ITEM_ACTION_BREAK_BLOCK = 2;
	
	public static final int RELEASE_ITEM_ACTION_RELEASE = 0; // bow shoot
	public static final int RELEASE_ITEM_ACTION_CONSUME = 1; // eat food, drink potion
	
	public static final int USE_ITEM_ON_ENTITY_ACTION_INTERACT = 0;
	public static final int USE_ITEM_ON_ENTITY_ACTION_ATTACK = 1;
	
	public int type;
	public boolean craftingPart;
	public InventoryTransactionAction[] actions;
	
	//constructor
	public InventoryTransactionPacket() {
		
	}
	
	//public
	public int pid() {
		return ProtocolInfo.INVENTORY_TRANSACTION_PACKET;
	}
	
	public void encodePayload() {
		putUnsignedVarInt(type);
		if (actions != null && actions.length > 0) {
			putUnsignedVarInt(actions.length);
			for (InventoryTransactionAction action : actions) {
				action.write(this);
			}
		} else {
			putUnsignedVarInt(0);
		}
	}
	public void decodePayload() {
		type = (int) getUnsignedVarInt();
		int len = (int) getUnsignedVarInt();
		actions = new InventoryTransactionAction[len];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				actions[i] = InventoryTransactionAction.read(this);
			}
		}

		// TODO: more to be added LATER
	}
	
	//private
	
}
