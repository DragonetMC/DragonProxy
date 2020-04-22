package org.dragonet.proxy.network.translator.misc.inventory;

import org.dragonet.proxy.data.window.BedrockWindowType;

public class DoubleChestInventoryTranslator extends SingleChestInventoryTranslator {

    public DoubleChestInventoryTranslator(int size, int rows) {
        super(BedrockWindowType.DOUBLE_CHEST, size, rows);
    }
}
