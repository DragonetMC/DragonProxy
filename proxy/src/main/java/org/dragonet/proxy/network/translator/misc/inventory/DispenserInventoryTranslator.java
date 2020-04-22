package org.dragonet.proxy.network.translator.misc.inventory;

import org.dragonet.proxy.data.window.BedrockWindowType;

public class DispenserInventoryTranslator extends ContainerInventoryTranslator {

    public DispenserInventoryTranslator(int size) {
        super(BedrockWindowType.DISPENSER, size);
    }
}
