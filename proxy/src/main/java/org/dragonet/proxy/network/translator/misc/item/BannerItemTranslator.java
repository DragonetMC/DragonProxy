package org.dragonet.proxy.network.translator.misc.item;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@ItemRegisterInfo(bedrockId = 446)
public class BannerItemTranslator implements IItemTranslator {

    @Override
    public ItemData translateToBedrock(ItemStack item, ItemEntry itemEntry) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        CompoundTag javaTag = item.getNbt();

        CompoundTag blockEntityTag = javaTag.get("BlockEntityTag");
        ListTag patterns = blockEntityTag.get("Patterns");
        log.warn(patterns.getValue());

        List<com.nukkitx.nbt.tag.CompoundTag> patternList = new ArrayList<>();
        for(Tag pattern : patterns) {
            patternList.add(convertPattern(pattern));
        }
        root.listTag("Patterns", com.nukkitx.nbt.tag.CompoundTag.class, patternList);
        
        return itemEntry.toItemData(item.getAmount(), root.buildRootTag());
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
