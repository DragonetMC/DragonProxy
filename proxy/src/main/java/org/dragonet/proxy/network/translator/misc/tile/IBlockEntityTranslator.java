package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;

public interface IBlockEntityTranslator {
    void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId);
}
