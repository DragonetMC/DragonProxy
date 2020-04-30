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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.data.GameRuleData;
import com.nukkitx.protocol.bedrock.packet.GameRulesChangedPacket;
import com.nukkitx.protocol.bedrock.packet.SettingsCommandPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = SettingsCommandPacket.class)
public class PESettingsCommandTranslator extends PacketTranslator<SettingsCommandPacket> {
    private static final Object2ObjectMap<String, String> gameRuleMap = new Object2ObjectOpenHashMap<>();

    static {
        // The new Java Edition command system is complete trash, and case matters; ugh
        gameRuleMap.put("commandblockoutput", "commandBlockOutput");
        gameRuleMap.put("commandblocksenabled", "commandBlocksEnabled");
        gameRuleMap.put("dodaylightcycle", "doDaylightCycle");
        gameRuleMap.put("doentitydrops", "doEntityDrops");
        gameRuleMap.put("dofiretick", "doFireTick");
        gameRuleMap.put("doimmediaterespawn", "doImmediateRespawn");
        gameRuleMap.put("doinsomnia", "doInsomnia");
        gameRuleMap.put("domobloot", "doMobLoot");
        gameRuleMap.put("domobspawning", "doMobSpawning");
        gameRuleMap.put("dotiledrops", "doTileDrops");
        gameRuleMap.put("doweathercycle", "doWeatherCycle");
        gameRuleMap.put("drowningdamage", "drowningDamage");
        gameRuleMap.put("falldamage", "fallDamage");
        gameRuleMap.put("firedamage", "fireDamage");
        // functionCommandLimit
        gameRuleMap.put("keepinventory", "keepInventory");
        gameRuleMap.put("maxcommandchainlength", "maxCommandChainLength");
        gameRuleMap.put("mobgriefing", "mobGriefing");
        gameRuleMap.put("naturalregeneration", "naturalRegeneration");
        gameRuleMap.put("pvp", "pvp");
        gameRuleMap.put("randomtickspeed", "randomTickSpeed");
        gameRuleMap.put("sendcommandfeedback", "sendCommandFeedback");
        // showCoordinates handled seperately
        gameRuleMap.put("showdeathmessages", "showDeathMessages");
        // showTags
        gameRuleMap.put("spawnradius", "spawnRadius");
        // tntExplodes
    }

    @Override
    public void translate(ProxySession session, SettingsCommandPacket packet) {
        WorldCache worldCache = session.getWorldCache();
        String[] args = packet.getCommand().replaceAll("/gamerule ", "").split(" ");

        // Coordinates are a special case
        if(args[0].equalsIgnoreCase("showcoordinates") && !session.getCachedEntity().isReducedDebugInfo()) {
            worldCache.setShowCoordinates(session, Boolean.parseBoolean(args[1]));
            return;
        }

        if(!gameRuleMap.containsKey(args[0])) {
            // TODO: reset the game rule on the client?
            return;
        }

        session.sendRemotePacket(new ClientChatPacket("/gamerule " + gameRuleMap.get(args[0]) + " " + args[1]));
    }
}
