package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.nukkitx.nbt.CompoundTagBuilder;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.util.DyeColor;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

@Log4j2
@BlockEntityRegisterInfo(bedrockId = "Bed")
public class BedBlockEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        if(!javaId.contains("_bed"))
            return;

        String color = javaId.substring(0, javaId.indexOf("[")).replace("_bed", "").replace("minecraft:", "");
        builder.byteTag("color", (byte) DyeColor.valueOf(color.toUpperCase()).getId());
    }
}
