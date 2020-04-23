package org.dragonet.proxy.network.translator.misc.item.nbt;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.CompoundTag;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;

public class DisplayNbtTranslator implements ItemNbtTranslator {

    @Override
    public CompoundTag translateToBedrock(com.github.steveice10.opennbt.tag.builtin.CompoundTag javaTag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        if(!javaTag.contains("display")) {
            CompoundTagBuilder display = CompoundTagBuilder.builder();

            if (javaTag.contains("name")) {
                display.stringTag("Name", Message.fromString((String) javaTag.get("name").getValue()).getFullText());
                javaTag.remove("name");
            }
            if (javaTag.contains("lore")) {
                com.nukkitx.nbt.tag.ListTag list = (com.nukkitx.nbt.tag.ListTag) ItemTranslatorRegistry.translateRawNBT((Tag) javaTag.get("lore"));
                // noinspection unchecked
                display.listTag("Lore", com.nukkitx.nbt.tag.StringTag.class, list.getValue());
                javaTag.remove("lore");
            }
            root.tag(display.build("display"));
        }
        return root.buildRootTag();
    }
}
