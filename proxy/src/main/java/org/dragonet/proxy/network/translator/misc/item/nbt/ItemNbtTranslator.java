package org.dragonet.proxy.network.translator.misc.item.nbt;

import com.nukkitx.nbt.tag.CompoundTag;

public interface ItemNbtTranslator {
    CompoundTag translateToBedrock(com.github.steveice10.opennbt.tag.builtin.CompoundTag javaTag);
}
