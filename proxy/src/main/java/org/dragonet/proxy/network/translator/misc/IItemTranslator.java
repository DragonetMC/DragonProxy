package org.dragonet.proxy.network.translator.misc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;
import org.dragonet.proxy.network.translator.misc.item.ItemEntry;

public interface IItemTranslator {
    ItemData translateToBedrock(ItemStack item, ItemEntry itemEntry);
}
