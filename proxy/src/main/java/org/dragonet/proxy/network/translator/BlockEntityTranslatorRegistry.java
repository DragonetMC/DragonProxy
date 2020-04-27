package org.dragonet.proxy.network.translator;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.MessageTranslator;
import org.dragonet.proxy.network.translator.misc.tile.IBlockEntityTranslator;
import org.dragonet.proxy.util.TextFormat;
import org.dragonet.proxy.util.registry.BlockEntityRegisterInfo;
import org.dragonet.proxy.util.registry.Registry;

import java.util.Map;

@Log4j2
public class BlockEntityTranslatorRegistry extends Registry {
    // Java to Bedrock block entity name map
    private static final Object2ObjectMap<String, String> blockEntityMap = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<String, String> legacyJavaToBedrockMap = new Object2ObjectOpenHashMap<>();

    private static final Object2ObjectMap<String, IBlockEntityTranslator> customTranslators = new Object2ObjectOpenHashMap<>();

    static {
        // Register custom block entity translators
        registerPath("org.dragonet.proxy.network.translator.misc.tile", BlockEntityRegisterInfo.class, (info ,clazz) -> {
            try {
                customTranslators.put(((BlockEntityRegisterInfo) info).bedrockId(), (IBlockEntityTranslator) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        register("minecraft:bed", "Bed");
        register("minecraft:chest", "Chest");
        register("minecraft:ender_chest", "EnderChest");
        register("minecraft:command_block", "CommandBlock");
        register("minecraft:sign", "Sign");
        register("minecraft:flower_pot", "FlowerPot");
        register("minecraft:hopper", "Hopper");
        register("minecraft:dropper", "Dropper");
        register("minecraft:dispenser", "Dispenser", "Trap");
        register("minecraft:daylight_detector", "DaylightDetector", "DLDetector");
        register("minecraft:shulker_box", "ShulkerBox");
        register("minecraft:furnace", "Furnace");
        register("minecraft:structure_block", "StructureBlock");
        register("minecraft:end_gateway", "EndGateway");
        register("minecraft:beacon", "Beacon");
        register("minecraft:end_portal", "EndPortal", "Airportal");
        register("minecraft:mob_spawner", "MobSpawner");
        register("minecraft:skull", "Skull");
        register("minecraft:banner", "Banner");
        register("minecraft:comparator", "Comparator");
        register("minecraft:jukebox", "Jukebox", "RecordPlayer");
        register("minecraft:piston", "PistonArm");
        register("minecraft:noteblock", "Noteblock");
        register("minecraft:enchanting_table", "EnchantTable");
        register("minecraft:brewing_stand", "BrewingStand");

        // Not sure about these ones
        register("minecraft:beehive", "Beehive");
        register("minecraft:bell", "Bell");
        register("minecraft:smoker", "Smoker");
        register("minecraft:blast_furnace", "BlastFurnace");
        register("minecraft:barrel", "Barrel");
        register("minecraft:campfire", "Campfire");
        register("minecraft:lectern", "Lectern");
    }

    private static void register(String javaId, String bedrockId) {
        blockEntityMap.put(javaId, bedrockId);
    }

    private static void register(String javaId, String bedrockId, String legacyJavaId) {
        blockEntityMap.put(javaId, bedrockId);
        blockEntityMap.put(legacyJavaId, bedrockId);
    }

    public static CompoundTag translateToBedrock(com.github.steveice10.opennbt.tag.builtin.CompoundTag javaTag) {
        CompoundTagBuilder root = CompoundTagBuilder.builder(); //ItemTranslate.translateRawNBT(javaTag).toBuilder()

        if(!javaTag.contains("id")) {
            log.debug("Tag does not contain id");
            return null;
        }

        String javaId = (String) javaTag.get("id").getValue();
        String bedrockId = getBedrockIdentifier(javaId);

        if(bedrockId == null) {
            log.info(TextFormat.GRAY + "(debug) Unhandled block entity: " + javaId);
            return null;
        }

        // Execute custom translators
        if(customTranslators.containsKey(bedrockId)) {
            customTranslators.get(bedrockId).translateToBedrock(root, javaTag);
        }

        root.stringTag("id", bedrockId);
        root.intTag("x", (int) javaTag.get("x").getValue());
        root.intTag("y", (int) javaTag.get("y").getValue());
        root.intTag("z", (int) javaTag.get("z").getValue());
        return root.buildRootTag();
    }

    public static void createPistonArm(ProxySession session, Vector3i position, boolean sticky) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        root.stringTag("id", "PistonArm")
            .floatTag("Progress", 1f)
            .byteTag("State", (byte) 1)
            .booleanTag("Sticky", sticky)
            .intTag("x", position.getX())
            .intTag("y", position.getY())
            .intTag("z", position.getZ());

        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition(position);
        blockEntityDataPacket.setData(root.buildRootTag());

        session.sendPacket(blockEntityDataPacket);
    }

    public static String getBedrockIdentifier(String javaIdentifier) {
        return blockEntityMap.get(javaIdentifier);
    }

    public static String getJavaIdentifier(String bedrockIdentifier) {
        for(Map.Entry<String, String> entry : blockEntityMap.entrySet()) {
            if(entry.getKey().startsWith("minecraft:") && entry.getValue().equals(bedrockIdentifier)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
