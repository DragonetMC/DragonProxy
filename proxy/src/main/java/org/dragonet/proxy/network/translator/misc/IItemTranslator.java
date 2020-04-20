package org.dragonet.proxy.network.translator.misc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;

public interface IItemTranslator {
    ItemStack translateToBedrock(ItemStack item);
}
