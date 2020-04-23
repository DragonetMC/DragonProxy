package org.dragonet.proxy.network.translator.misc.item.nbt;

import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import org.dragonet.proxy.data.EnchantmentType;

public class EnchantmentNbtTranslator implements ItemNbtTranslator {

    @Override
    public com.nukkitx.nbt.tag.CompoundTag translateToBedrock(CompoundTag javaTag) {
        if(!javaTag.contains("Enchantments")) {
            return null;
        }
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        ListTag enchantTag = javaTag.get("Enchantments");
        enchantTag.forEach(tag -> {
            if(tag instanceof CompoundTag) {
                StringTag id = ((CompoundTag) tag).get("id");
                ShortTag lvl = ((CompoundTag) tag).get("lvl");

                EnchantmentType enchantmentType = EnchantmentType.valueOf(id.getValue().replace("minecraft:", "").toUpperCase());
                if(enchantmentType == null) {
                    return;
                }

                root.shortTag("id", (short) enchantmentType.ordinal());
                root.shortTag("lvl", lvl.getValue());
            }
        });
        return root.buildRootTag();
    }
}
