package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.ItemData;
import org.dragonet.proxy.network.translator.types.item.ItemEntry;

import java.util.HashMap;
import java.util.Map;

public class ItemTranslator {
    public static final Map<Integer, ItemEntry.JavaItem> BEDROCK_TO_JAVA = new HashMap<>();
    public static final Map<Integer, ItemEntry.BedrockItem> JAVA_TO_BEDROCK = new HashMap<>();

    public static ItemData translateToBedrock(ItemStack item) {
        // TODO (this translates a stick currently)
        if(item.getId() == 545) {
            ItemData data = ItemData.of(280, (short) 0, item.getAmount());
            return data;
        }
        return ItemData.AIR;
    }

    public static ItemData translateSlotToBedrock(ItemStack item) {
        if(item == null || item.getId() == 0) {
            return ItemData.AIR;
        }

        ItemData data = translateToBedrock(item);
        return data;
    }
}
