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
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.misc.IItemTranslator;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.item.ItemEntry;
import org.dragonet.proxy.util.registry.ItemRegisterInfo;
import org.dragonet.proxy.util.registry.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class ItemTranslatorRegistry extends Registry {
    private static Int2ObjectMap<IItemTranslator> customTranslators = new Int2ObjectOpenHashMap<>(); // bedrock id

    private static final Int2ObjectMap<ItemEntry> javaToBedrockMap = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ItemEntry> bedrockToJavaMap = new Int2ObjectOpenHashMap<>();

    private static final AtomicInteger javaIdAllocator = new AtomicInteger(0);


    static {
        registerType(ItemRegisterInfo.class);
        registerPath("org.dragonet.proxy.network.translator.misc.item", (info, clazz) -> {
            try {
                customTranslators.put(((ItemRegisterInfo) info).bedrockId(), (IItemTranslator) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    static {
        InputStream stream = DragonProxy.class.getClassLoader().getResourceAsStream("mappings/1.15/item_mappings.json");
        if (stream == null) {
            throw new AssertionError("Item mapping table not found");
        }

        Map<String, ItemTranslatorRegistry.ItemMappingEntry> itemEntries;
        try {
            itemEntries = DragonProxy.JSON_MAPPER.readValue(stream, new TypeReference<Map<String, ItemTranslatorRegistry.ItemMappingEntry>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse item mappings", e);
        }

        itemEntries.forEach((javaIdentifier, itemMappingEntry) -> {
            int javaProtocolId = javaIdAllocator.getAndIncrement(); // Entries are loaded sequentially in the mapping file

            javaToBedrockMap.put(javaProtocolId, new ItemEntry(javaIdentifier, javaProtocolId, itemMappingEntry.getBedrockId(), itemMappingEntry.getBedrockData()));
            bedrockToJavaMap.put(itemMappingEntry.getBedrockId(), new ItemEntry(javaIdentifier, javaProtocolId, itemMappingEntry.getBedrockId(), itemMappingEntry.getBedrockData()));
        });
    }

    public static ItemData translateToBedrock(ItemStack item) {
        if(item == null || !javaToBedrockMap.containsKey(item.getId())) {
            return ItemData.AIR;
        }
        ItemEntry bedrockItem = javaToBedrockMap.get(item.getId());

        // Custom item translation. TODO: make this better
        if(customTranslators.containsKey(bedrockItem.getBedrockRuntimeId())) {
           // item = customTranslators.get(bedrockItem.getBedrockRuntimeId()).translateToBedrock(item);
        }

        if (item.getNbt() == null|| bedrockItem.getBedrockRuntimeId() == 397) { // Fix skull NBT crashing the client
            return ItemData.of(bedrockItem.getBedrockRuntimeId(), (short) bedrockItem.getBedrockData(), item.getAmount());
        }

        return ItemData.of(bedrockItem.getBedrockRuntimeId(), (short) bedrockItem.getBedrockData(), item.getAmount(), translateItemNBT(item.getNbt()));
    }

    public static ItemData translateSlotToBedrock(ItemStack item) {
        if(item == null || item.getId() == 0) {
            return ItemData.AIR;
        }

        ItemData data = translateToBedrock(item);
        return data;
    }

    public static ItemStack translateToJava(ItemData item) {
        if(item == null || !bedrockToJavaMap.containsKey(item.getId())) {
            return new ItemStack(0);
        }

        ItemEntry javaItem = bedrockToJavaMap.get(item.getId());
//        log.warn("ITEM NAME: " + javaItem.getJavaIdentifier());
        return new ItemStack(javaItem.getJavaProtocolId(), item.getCount());
    }

    public static com.nukkitx.nbt.tag.CompoundTag translateItemNBT(CompoundTag tag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();

        // First handle NBT that applies to all items
        if(!tag.contains("display")) {
            CompoundTagBuilder display = CompoundTagBuilder.builder();

            if (tag.contains("name")) {
                display.stringTag("Name", Message.fromString((String) tag.get("name").getValue()).getFullText());
                tag.remove("name");
            }
            if (tag.contains("lore")) {
                com.nukkitx.nbt.tag.ListTag list = (com.nukkitx.nbt.tag.ListTag) translateRawNBT((Tag) tag.get("lore"));
                display.listTag("Lore", com.nukkitx.nbt.tag.StringTag.class, list.getValue()); // TODO: fix unchecked assignment
                tag.remove("lore");
            }
            root.tag(display.build("display"));
        }

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
    private static class ItemMappingEntry {
        @JsonProperty("bedrock_id")
        private int bedrockId;

        @JsonProperty("bedrock_data")
        private int bedrockData;
    }
}
