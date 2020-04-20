package org.dragonet.proxy.network.translator.misc.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.LongTag;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

@ItemRegisterInfo(bedrockId = 358)
public class MapItemTranslator implements IItemTranslator {

    @Override
    public ItemStack translateToBedrock(ItemStack item) {
        if(item.getNbt() == null) {
            return item;
        }
        //CompoundTagBuilder root = CompoundTagBuilder.builder();

        com.github.steveice10.opennbt.tag.builtin.CompoundTag tag = item.getNbt();
        com.github.steveice10.opennbt.tag.builtin.IntTag id = tag.get("map");

        if(id != null) {
            tag.put(new LongTag("map_uuid", id.getValue().longValue()));
        }
        return item;
    }
}
