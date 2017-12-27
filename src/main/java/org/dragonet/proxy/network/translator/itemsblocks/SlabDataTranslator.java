package org.dragonet.proxy.network.translator.itemsblocks;

public class SlabDataTranslator extends IItemDataTranslator {

	@Override
	public Integer translateToPC(Integer damage) {
		System.out.println(damage);
		return damage;
	}

	@Override
	public Integer translateToPE(Integer damage) {
		System.out.println(damage);
		if(damage == 6) return 7;
		if(damage == 7) return 6;
		if(damage == 14) return 15;
		if(damage == 15) return 14;
		return damage;
	}
}
