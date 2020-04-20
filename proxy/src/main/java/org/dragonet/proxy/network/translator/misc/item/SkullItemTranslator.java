package org.dragonet.proxy.network.translator.misc.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

@ItemRegisterInfo(bedrockId = 397)
public class SkullItemTranslator implements IItemTranslator {

    @Override
    public ItemStack translateToBedrock(ItemStack item) {
        if(item.getNbt() == null) {
            return item;
        }
        CompoundTag tag = item.getNbt();
        CompoundTag displayTag = null;

        // Check if it contains the display info
        if(tag.contains("display")) {
            displayTag = tag.get("display");
        }

        // Clear the tag
        tag.clear();

        // Add back the display info
        if(displayTag != null) {
            tag.put(displayTag);
        }
        return item;
    }
}
