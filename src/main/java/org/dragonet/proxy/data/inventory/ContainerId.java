package org.dragonet.proxy.data.inventory;

/**
 * Created on 2017/10/21.
 */
public enum ContainerId {
	NONE(1), INVENTORY(0), FIRST(1), LAST(100), OFFHAND(119), ARMOR(120), CREATIVE(121), HOTBAR(122), FIXED_INVENTORY(
			123), CURSOR(124);

	private int id;

	private ContainerId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
