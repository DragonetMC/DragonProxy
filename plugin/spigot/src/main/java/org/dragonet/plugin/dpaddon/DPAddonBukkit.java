/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.plugin.dpaddon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.common.utilities.BinaryStream;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.plugin.bukkit.DPPluginMessageListener;

import java.util.HashSet;
import java.util.Set;

public class DPAddonBukkit extends JavaPlugin implements Listener {

    private static DPAddonBukkit instance;

    public static DPAddonBukkit getInstance() {
        return instance;
    }

    private DPPluginMessageListener pluginMessageListener;

    private final Set<Player> bedrockPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Registering event listener... ");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Registering channels... ");
        pluginMessageListener = new DPPluginMessageListener(this);
        getServer().getMessenger().registerIncomingPluginChannel(this, "DragonProxy", pluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "DragonProxy");
    }

    public void detectedBedrockPlayer(Player player) {
        getLogger().info("Detected bedrock player: " + player.getName());
        bedrockPlayers.add(player);

        BedrockPlayer.createForPlayer(player);

        // enable packet foward
        BinaryStream bis = new BinaryStream();
        bis.putString("PacketForward");
        bis.putBoolean(true);
        player.sendPluginMessage(this, "DragonProxy", bis.getBuffer());
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e) {
        if(bedrockPlayers.contains(e.getPlayer())) {
            bedrockPlayers.remove(e.getPlayer());
        }
    }

    public boolean isPlayerFromBedrock(Player player) {
        return bedrockPlayers.contains(player);
    }
}
