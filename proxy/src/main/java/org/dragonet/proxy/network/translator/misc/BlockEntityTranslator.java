/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.misc;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class BlockEntityTranslator {
    // Java to Bedrock block entity name map
    private static final Object2ObjectMap<String, String> blockEntityMap = new Object2ObjectOpenHashMap<>();

    static {
        blockEntityMap.put("minecraft:bed", "Bed");
        blockEntityMap.put("minecraft:chest", "Chest");
        blockEntityMap.put("minecraft:ender_chest", "EnderChest");
        blockEntityMap.put("minecraft:command_block", "CommandBlock");
        blockEntityMap.put("minecraft:sign", "Sign");
        blockEntityMap.put("minecraft:flower_pot", "FlowerPot");
        blockEntityMap.put("minecraft:hopper", "Hopper");
        blockEntityMap.put("minecraft:dropper", "Dropper");
        blockEntityMap.put("minecraft:dispenser", "Dispenser");
        blockEntityMap.put("minecraft:daylight_detector", "DaylightDetector");
        blockEntityMap.put("minecraft:shulker_box", "ShulkerBox");
        blockEntityMap.put("minecraft:furnace", "Furnace");
        blockEntityMap.put("minecraft:structure_block", "StructureBlock");
        blockEntityMap.put("minecraft:end_gateway", "EndGateway");
        blockEntityMap.put("minecraft:beacon", "Beacon");
        blockEntityMap.put("minecraft:end_portal", "EndPortal");
        blockEntityMap.put("minecraft:mob_spawner", "MobSpawner");
        blockEntityMap.put("minecraft:skull", "Skull");
        blockEntityMap.put("minecraft:banner", "Banner");
        blockEntityMap.put("minecraft:comparator", "Comparator");
        blockEntityMap.put("minecraft:jukebox", "Jukebox");
        blockEntityMap.put("minecraft:piston", "PistonArm");
        blockEntityMap.put("minecraft:noteblock", "Noteblock");
        blockEntityMap.put("minecraft:enchanting_table", "EnchantTable");
        blockEntityMap.put("minecraft:brewing_stand", "BrewingStand");

        // Not sure about these ones
        blockEntityMap.put("minecraft:beehive", "Beehive");
        blockEntityMap.put("minecraft:bell", "Bell");
        blockEntityMap.put("minecraft:smoker", "Smoker");
        blockEntityMap.put("minecraft:blast_furnace", "BlastFurnace");
        blockEntityMap.put("minecraft:barrel", "Barrel");
        blockEntityMap.put("minecraft:campfire", "Campfire");
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
            bedrockId = javaId; // Fall back
            return null;
        }

        // TODO: bed colour
        //log.info(javaTag.getValue());

        switch(bedrockId) {
            case "Beacon":
                // TODO: see above, make this automatically translate
                root.intTag("Primary", (int) javaTag.get("Primary").getValue());
                root.intTag("Secondary", (int) javaTag.get("Secondary").getValue());
                root.intTag("Levels", (int) javaTag.get("Levels").getValue());
                root.stringTag("Lock", "");
                break;
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
            if(entry.getValue().equals(bedrockIdentifier)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
