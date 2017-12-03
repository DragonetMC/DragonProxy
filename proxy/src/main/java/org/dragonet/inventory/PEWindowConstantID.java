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
package org.dragonet.inventory;

public enum PEWindowConstantID {
	PLAYER_INVENTORY(0x00), PLAYER_ARMOR(0x78), PLAYER_CREATIVE(0x79);

	private final byte id;

	PEWindowConstantID(int id) {
		this.id = (byte) id;
	}

	public byte getId() {
		return id;
	}
}
