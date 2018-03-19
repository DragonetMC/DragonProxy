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
package org.dragonet.plugin.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.common.utilities.BinaryStream;
import org.dragonet.plugin.bukkit.commands.DragonProxyFormCommand;
import org.dragonet.plugin.bukkit.compat.luckperms.LuckPermsCompat;
import org.dragonet.plugin.bukkit.events.ModalFormResponseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DPAddonBukkit extends JavaPlugin implements Listener {

    private static DPAddonBukkit instance;

    public static DPAddonBukkit getInstance() {
        return instance;
    }

    private DPPluginMessageListener pluginMessageListener;

    private final Set<UUID> bedrockPlayers = new HashSet<>();
    
    private boolean isPluginLoaded(String pluginName) {
      return getServer().getPluginManager().getPlugin(pluginName) != null;
  }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Registering event listener... ");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Registering channels... ");
        pluginMessageListener = new DPPluginMessageListener(this);
        getServer().getMessenger().registerIncomingPluginChannel(this, "DragonProxy", pluginMessageListener);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "DragonProxy");
        this.getCommand("form").setExecutor(new DragonProxyFormCommand(this));
        
        if (isPluginLoaded("LuckPerms")) {
            LuckPermsCompat.addContextCalculator(bedrockPlayers);
        }
    }

    public void detectedBedrockPlayer(Player player) {
        getLogger().info("Detected bedrock player: " + player.getName() + " " + player.getUniqueId().toString());
        bedrockPlayers.add(player.getUniqueId());

        BedrockPlayer.createForPlayer(player);

        // enable packet foward
        BinaryStream bis = new BinaryStream();
        bis.putString("PacketForward");
        bis.putBoolean(true);
        player.sendPluginMessage(this, "DragonProxy", bis.getBuffer());
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        if(bedrockPlayers.contains(event.getPlayer().getUniqueId())) {
            if (bedrockPlayers.remove(event.getPlayer().getUniqueId())) {
                getLogger().info("Disconnected Bedrock player " + event.getPlayer().getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void onModalFormResponse(ModalFormResponseEvent event) {
        getLogger().info("Player " + event.getBedrockPlayer().getPlayer().getName() + " validate form " + event.getValues());
    }

    public boolean isBedrockPlayer(UUID uuid) {
        return bedrockPlayers.contains(uuid);
    }
}
