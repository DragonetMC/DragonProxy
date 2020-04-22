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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Pose;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.nukkitx.protocol.bedrock.data.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedItemEntity;
import org.dragonet.proxy.network.session.cache.object.CachedItemFrame;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.entity.living.*;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;
import org.dragonet.proxy.network.translator.misc.entity.object.FishHookMetaTranslator;
import org.dragonet.proxy.network.translator.misc.entity.object.ItemEntityMetaTranslator;
import org.dragonet.proxy.network.translator.misc.entity.object.ItemFrameMetaTranslator;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class EntityMetaTranslator {
    private static final Object2ObjectMap<BedrockEntityType, IMetaTranslator> translatorMap = new Object2ObjectOpenHashMap<>();

    private static final ItemFrameMetaTranslator itemFrameTranslator = new ItemFrameMetaTranslator();

    static {
        translatorMap.put(BedrockEntityType.ITEM, new ItemEntityMetaTranslator());
        translatorMap.put(BedrockEntityType.FISHING_HOOK, new FishHookMetaTranslator());
        translatorMap.put(BedrockEntityType.CREEPER, new CreeperMetaTranslator());
        translatorMap.put(BedrockEntityType.WOLF, new WolfMetaTranslator());
        translatorMap.put(BedrockEntityType.HORSE, new HorseMetaTranslator());
        translatorMap.put(BedrockEntityType.OCELOT, new OcelotMetaTranslator());
        translatorMap.put(BedrockEntityType.PIG, new PigMetaTranslator());
        translatorMap.put(BedrockEntityType.ZOMBIE, new ZombieMetaTranslator());
        translatorMap.put(BedrockEntityType.ZOMBIE_VILLAGER, new ZombieVillagerMetaTranslator());
        translatorMap.put(BedrockEntityType.WITHER, new WitherMetaTranslator());
        translatorMap.put(BedrockEntityType.SHEEP, new SheepMetaTranslator());
        translatorMap.put(BedrockEntityType.BLAZE, new BlazeMetaTranslator());
        translatorMap.put(BedrockEntityType.ENDERMAN, new EndermanMetaTranslator());
        translatorMap.put(BedrockEntityType.CAT, new CatMetaTranslator());
        translatorMap.put(BedrockEntityType.SNOW_GOLEM, new SnowGolemMetaTranslator());
        translatorMap.put(BedrockEntityType.IRON_GOLEM, new IronGolemMetaTranslator());
        translatorMap.put(BedrockEntityType.ENDER_DRAGON, new EnderDragonMetaTranslator());
        translatorMap.put(BedrockEntityType.SPIDER, new SpiderMetaTranslator());
        translatorMap.put(BedrockEntityType.VILLAGER_V2, new VillagerMetaTransformer());
    }

    /**
     * This method translates Java entity metadata to Bedrock.
     */
    public static EntityDataMap translateToBedrock(ProxySession session, CachedEntity entity, EntityMetadata[] metadata) {
        EntityDataMap dictionary = entity.getMetadata();
        EntityFlags flags = dictionary.getFlags();

        for(EntityMetadata meta : metadata) {
            // HACK FOR ITEM FRAMES
            if(entity instanceof CachedItemFrame) {
                itemFrameTranslator.setEntity(entity);
                itemFrameTranslator.translateToBedrock(session, dictionary, meta);
                return null;
            }

            switch(meta.getId()) {
                case 0: // Flags
                    byte javaFlags = (byte) meta.getValue();
                    flags.setFlag(EntityFlag.ON_FIRE, (javaFlags & 0x01) > 0);
                    flags.setFlag(EntityFlag.SNEAKING, (javaFlags & 0x02) > 0);
                    flags.setFlag(EntityFlag.SPRINTING, (javaFlags & 0x08) > 0);
                    flags.setFlag(EntityFlag.SWIMMING, (javaFlags & 0x10) > 0);
                    flags.setFlag(EntityFlag.GLIDING, (javaFlags & 0x80) > 0);

                    if((javaFlags & 0x20) > 0) { // Invisible
                        // HACK! Setting the invisible flag will also hide the nametag on bedrock,
                        // so this hack is needed to simulate invisibility.
                        dictionary.put(EntityData.SCALE, 0.01f);
                    } else {
                        dictionary.put(EntityData.SCALE, entity.getScale());
                    }
                    break;
                case 1: // Air
                    dictionary.putShort(EntityData.AIR, (short) (int) meta.getValue());
                    break;
                case 2: // Custom name
                    if(meta.getValue() != null) {
                        dictionary.putString(EntityData.NAMETAG, MessageTranslator.translate((Message) meta.getValue()));
                    }
                    break;
                case 3: // Is custom name visible
                    if(!(entity instanceof CachedPlayer)) {
                        dictionary.putByte(EntityData.ALWAYS_SHOW_NAMETAG, (boolean) meta.getValue() ? (byte) 1 : (byte) 0);
                    }
                    break;
                case 4: // Is silent
                    flags.setFlag(EntityFlag.SILENT, (boolean) meta.getValue());
                    break;
                case 5: // No gravity
                    flags.setFlag(EntityFlag.HAS_GRAVITY, !(boolean) meta.getValue()); // flipped intentionally
                    break;
                case 6: // Pose
                    break;
                default:
                    if(translatorMap.containsKey(entity.getEntityType())) {
                        // Kinda a hack because im too lazy to change the parameters, but oh well
                        translatorMap.get(entity.getEntityType()).setEntity(entity);
                        translatorMap.get(entity.getEntityType()).translateToBedrock(session, dictionary, meta);
                        break;
                    }
                    //log.info("No meta translator for " + entity.getEntityType().name());
                    break;
            }
        }

        return dictionary;
    }
}
