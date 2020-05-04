package org.dragonet.proxy.network.translator.misc.tile;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.nukkitx.nbt.CompoundTagBuilder;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.util.DyeColor;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@BlockEntityRegisterInfo(bedrockId = "Banner")
public class BannerEntityTranslator implements IBlockEntityTranslator {

    @Override
    public void translateToBedrock(CompoundTagBuilder builder, CompoundTag javaTag, String javaId) {
        if(!javaId.contains("banner"))
            return;

        String bannerType = javaId.contains("_wall_banner") ? "_wall_banner" : "_banner";
        String color = javaId.substring(0, javaId.indexOf("[")).replace(bannerType, "").replace("minecraft:", "");

        builder.intTag("Base", 15 - DyeColor.valueOf(color.toUpperCase()).getId());
        builder.intTag("Type", 0);

        if(javaTag.contains("Patterns")) {
            List<com.nukkitx.nbt.tag.CompoundTag> patternList = new ArrayList<>();
            for(Tag pattern : ((ListTag) javaTag.get("Patterns")).getValue()) {
                patternList.add(convertPattern(pattern));
            }
            builder.listTag("Patterns", com.nukkitx.nbt.tag.CompoundTag.class, patternList);
        }
    }

    private com.nukkitx.nbt.tag.CompoundTag convertPattern(Tag tag) {
        CompoundTag pattern = (CompoundTag) tag;

        //According to Minecraft wiki this is a java only pattern
        if(pattern.equals("glb"))
            return null;

        return CompoundTagBuilder.builder()
        .stringTag("Pattern", pattern.get("Pattern").getValue().toString())
        .intTag("Color", 15 - Integer.parseInt(pattern.get("Color").getValue().toString()))
        .buildRootTag();
    }
}
