package org.dragonet.proxy.network.translator.misc.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.protocol.bedrock.data.ItemData;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

@ItemRegisterInfo(bedrockId = 397)
public class SkullItemTranslator implements IItemTranslator {

    @Override
    public ItemData translateToBedrock(ItemStack item, ItemEntry itemEntry) {
        if(item.getNbt() == null) {
            return itemEntry.toItemData(item.getAmount());
        }
        CompoundTag javaTag = item.getNbt();

        // Check if it contains the display info
        if(javaTag.contains("display")) {
            CompoundTag tag = new CompoundTag("")
                .put(javaTag.get("display"));

            return itemEntry.toItemData(item.getAmount(), ItemTranslatorRegistry.translateItemNBT(tag));
        }
        return itemEntry.toItemData(item.getAmount());
    }
}
