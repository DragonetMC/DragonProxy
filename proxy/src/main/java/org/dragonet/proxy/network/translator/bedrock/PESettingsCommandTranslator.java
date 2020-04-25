package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.SettingsCommandPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.WorldCache;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;

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
        if(args[0].equalsIgnoreCase("showcoordinates")) {
            worldCache.setShowCoordinates(session, Boolean.parseBoolean(args[1]));
            return;
        }

        if(!gameRuleMap.containsKey(args[0])) {
            // TODO: reset the game rule on the client
            return;
        }

        session.sendRemotePacket(new ClientChatPacket("/gamerule " + gameRuleMap.get(args[0]) + " " + args[1]));
    }
}
