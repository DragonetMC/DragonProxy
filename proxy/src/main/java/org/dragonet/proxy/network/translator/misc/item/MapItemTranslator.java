package org.dragonet.proxy.network.translator.misc.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

@Log4j2
@ItemRegisterInfo(bedrockId = 358)
public class MapItemTranslator implements IItemTranslator {

    @Override
    public ItemData translateToBedrock(ItemStack item, ItemEntry itemEntry) {
        if(item.getNbt() == null) {
            return itemEntry.toItemData(item.getAmount());
        }
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        CompoundTag javaTag = item.getNbt();

        root.tag(ItemTranslatorRegistry.translateItemNBT(javaTag));

        if(javaTag.contains("map")) {
            root.longTag("map_uuid", (long) javaTag.get("map").getValue());
        }
        return itemEntry.toItemData(item.getAmount(), root.buildRootTag());
    }
}
