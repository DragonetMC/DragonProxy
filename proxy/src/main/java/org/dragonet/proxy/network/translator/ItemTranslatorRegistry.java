package org.dragonet.proxy.network.translator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.item.ItemEntry;
import org.dragonet.proxy.network.translator.misc.item.nbt.DisplayNbtTranslator;
import org.dragonet.proxy.network.translator.misc.item.nbt.EnchantmentNbtTranslator;
import org.dragonet.proxy.network.translator.misc.item.nbt.ItemNbtTranslator;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;
import org.dragonet.proxy.util.registry.MappingEntry;
import org.dragonet.proxy.util.registry.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class ItemTranslatorRegistry extends Registry {
    private static List<ItemNbtTranslator> nbtTranslators = new ArrayList<>();
    private static Int2ObjectMap<IItemTranslator> customTranslators = new Int2ObjectOpenHashMap<>(); // bedrock id

    private static final Int2ObjectMap<ItemEntry> javaToBedrockMap = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ItemEntry> bedrockToJavaMap = new Int2ObjectOpenHashMap<>();

    private static final AtomicInteger javaIdAllocator = new AtomicInteger(0);


    static {
        // Register custom item translators
        registerPath("org.dragonet.proxy.network.translator.misc.item", ItemRegisterInfo.class, (info, clazz) -> {
            try {
                customTranslators.put(((ItemRegisterInfo) info).bedrockId(), (IItemTranslator) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        // Load item mappings from disk
        registerMapping("mappings/1.15/item_mappings.json", ItemMappingEntry.class, itemEntries -> {
            itemEntries.forEach((key, value) -> {
                ItemMappingEntry itemMappingEntry = (ItemMappingEntry) value;
                int javaProtocolId = javaIdAllocator.getAndIncrement(); // Entries are loaded sequentially in the mapping file

                javaToBedrockMap.put(javaProtocolId, new ItemEntry(key, javaProtocolId, itemMappingEntry.getBedrockId(), itemMappingEntry.getBedrockData()));
                bedrockToJavaMap.put(itemMappingEntry.getBedrockId(), new ItemEntry(key, javaProtocolId, itemMappingEntry.getBedrockId(), itemMappingEntry.getBedrockData()));
            });
        });

        nbtTranslators.add(new DisplayNbtTranslator());
        nbtTranslators.add(new EnchantmentNbtTranslator());
    }

    public static ItemData translateToBedrock(ItemStack item) {
        if(item == null || item.getId() == 0 || !javaToBedrockMap.containsKey(item.getId())) {
            return ItemData.AIR;
        }
        ItemEntry itemEntry = javaToBedrockMap.get(item.getId());

        // Custom item translation. TODO: make this better
        if(customTranslators.containsKey(itemEntry.getBedrockRuntimeId())) {
          return customTranslators.get(itemEntry.getBedrockRuntimeId()).translateToBedrock(item, itemEntry);
        }

        if (item.getNbt() == null || itemEntry.getBedrockRuntimeId() == 397) { // Fix skull NBT crashing the client
            return itemEntry.toItemData(item.getAmount());
        }

        return itemEntry.toItemData(item.getAmount(), translateItemNBT(item.getNbt()));
    }

    public static ItemStack translateToJava(ItemData item) {
        if(item == null || item.getId() == 0 || !bedrockToJavaMap.containsKey(item.getId())) {
            return new ItemStack(0);
        }

        ItemEntry javaItem = bedrockToJavaMap.get(item.getId());
        return new ItemStack(javaItem.getJavaProtocolId(), item.getCount());
    }

    public static com.nukkitx.nbt.tag.CompoundTag translateItemNBT(CompoundTag tag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        nbtTranslators.forEach(translator -> {
            com.nukkitx.nbt.tag.CompoundTag bedrockTag = translator.translateToBedrock(tag);
            if(bedrockTag != null) {
                root.tag(bedrockTag);
            }
        });

        if(tag.getValue() != null && !tag.getValue().isEmpty()) {
            for(String tagName : tag.getValue().keySet()) {
                com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT((Tag) tag.get(tagName));
                if(bedrockTag == null) {
                    continue;
                }
                root.tag(bedrockTag);
            }
        }
        return root.buildRootTag();
    }

    public static com.nukkitx.nbt.tag.CompoundTag translateRawNBT(CompoundTag tag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        if(tag.getValue() != null && !tag.getValue().isEmpty()) {
            for(String tagName : tag.getValue().keySet()) {
                com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT((Tag) tag.get(tagName));
                if(bedrockTag == null) {
                    continue;
                }
                root.tag(bedrockTag);
            }
        }

        return root.buildRootTag();
    }

    public static com.nukkitx.nbt.tag.Tag translateRawNBT(Tag tag) {
        if(tag instanceof ByteArrayTag) {
            return new com.nukkitx.nbt.tag.ByteArrayTag(tag.getName(), (byte[]) tag.getValue());
        }
        if(tag instanceof StringTag) {
            return new com.nukkitx.nbt.tag.StringTag(tag.getName(), MessageTranslator.translate(((String) tag.getValue())));
        }
        if(tag instanceof ListTag) {
            ListTag listTag = (ListTag) tag;
            List<com.nukkitx.nbt.tag.Tag> tags = new ArrayList<>();

            for(Object value : listTag.getValue()) {
                if(!(value instanceof Tag)) {
                    continue;
                }
                Tag tagValue = (Tag) value;
                com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT(tagValue);
                if(bedrockTag != null) {
                    tags.add(bedrockTag);
                }
            }
            // TODO: map element type from Java NBT tag to NukkitX tag
            return new com.nukkitx.nbt.tag.ListTag(listTag.getName(), com.nukkitx.nbt.tag.StringTag.class, tags);
        }
        if(tag instanceof CompoundTag) {
            CompoundTag compound = (CompoundTag) tag;
            CompoundTagBuilder builder = CompoundTagBuilder.builder();

            if(compound.getValue() != null && !compound.getValue().isEmpty()) {
                for(String tagName : compound.getValue().keySet()) {
                    com.nukkitx.nbt.tag.Tag bedrockTag = translateRawNBT((Tag) compound.get(tagName));
                    if(bedrockTag == null) {
                        continue;
                    }
                    builder.tag(bedrockTag);
                }
                return builder.build(tag.getName());
            }
        }

        return null;
    }

    @Getter
    private static class ItemMappingEntry implements MappingEntry {
        @JsonProperty("bedrock_id")
        private int bedrockId;

        @JsonProperty("bedrock_data")
        private int bedrockData;
    }
}
