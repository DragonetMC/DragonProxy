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
package org.dragonet.plugin.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.dragonet.common.utilities.BinaryStream;
import org.dragonet.common.utilities.LoginChainDecoder;
import org.dragonet.common.utilities.ReflectedClass;
import org.dragonet.plugin.bungeecord.compat.luckperms.LuckPermsCompat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DPAddonBungee extends Plugin implements Listener {

    private static DPAddonBungee instance;
    private Config config;
//    private List<InetAddress> whitelist = new ArrayList();

    public final Set<UUID> bedrockPlayers = Collections.synchronizedSet(new HashSet<>());

    public static DPAddonBungee getInstance() {
        return instance;
    }
    
    private boolean isPluginLoaded(String pluginName) {
      return getProxy().getPluginManager().getPlugin(pluginName) != null;
  }

    @Override
    public void onLoad() {
        // Save the plugin instance
        instance = this;

        // Init config
        config = new Config(this);
        config.load("config.yml");

//        List<String> bypassIPs = config.getConfiguration().getStringList("auth_bypass_ip");
//        for(String ip : bypassIPs) {
//            try {
//                whitelist.add(InetAddress.getByName("0.0.0.0"));
//            } catch (UnknownHostException ex) {
//                Logger.getLogger(DPAddonBungee.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
        if (isPluginLoaded("LuckPerms")) {
            LuckPermsCompat.addContextCalculator();
        }
    }

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreLogin(PreLoginEvent event) {
        if (ProxyServer.getInstance().getConfig().isOnlineMode()) {
            event.registerIntent(this);
            ProxyServer.getInstance().getScheduler().runAsync(this, new Runnable() {
                @Override
                public void run() {
                    String extraDataInHandshake = ((InitialHandler) event.getConnection()).getExtraDataInHandshake();
                    List<String> bypassIPs = config.getConfiguration().getStringList("auth_bypass_ip");

                    if (!bypassIPs.isEmpty() && bypassIPs.contains(event.getConnection().getAddress().getHostString())) { // check if IP
                        String[] xboxliveProfileParts = extraDataInHandshake.split("\0");
                        if (xboxliveProfileParts.length == 2) {
                            LoginChainDecoder decoder = new LoginChainDecoder(xboxliveProfileParts[1].getBytes(), null);
                            try {
                                decoder.decode(); //verify login chain, extract players UUID and name
                            } catch (NullPointerException ex) {

                            }
                            if (decoder.isLoginVerified()) {
                                ReflectedClass initialHandler = new ReflectedClass(event.getConnection());
                                initialHandler.setField("name", decoder.username);
                                initialHandler.setField("uniqueId", decoder.clientUniqueId);
                                event.getConnection().setOnlineMode(false);
                                getLogger().info("Bedrock player " + decoder.username + " uuid : " + decoder.clientUniqueId + " injected in InitialHandler !");
                                bedrockPlayers.add(event.getConnection().getUniqueId());
                            } else {
                                getLogger().info("Bedrock player Fail to verify XBox Identity " + decoder.username);
                                event.getConnection().disconnect(TextComponent.fromLegacyText("Fail to verify XBox Identity, please login to XboxLive and retry."));
                            }
                        }
                    }
                    event.completeIntent(instance);
                }
            });
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        if (!bedrockPlayers.contains(event.getPlayer().getUniqueId()))
            return;
        // forward the DragonProxy Notification message!
        getProxy().getScheduler().schedule(this, () -> {
            BinaryStream bis = new BinaryStream();
            bis.putString("Notification");
            event.getPlayer().sendData("DragonProxy", bis.getBuffer());
        }, 2000L, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (!bedrockPlayers.contains(event.getPlayer().getUniqueId()))
            return;
        // We don't know that another server supports forwarding or not so we disable forwarding for now!
        BinaryStream bis = new BinaryStream();
        bis.putString("PacketFoward");
        bis.putBoolean(false);
        event.getPlayer().sendData("DragonProxy", bis.getBuffer());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (bedrockPlayers.contains(event.getPlayer().getUniqueId()))
            if (bedrockPlayers.remove(event.getPlayer().getUniqueId()))
                getLogger().info("Disconnected Bedrock player " + event.getPlayer().getUniqueId().toString());
    }

    public boolean isBedrockPlayer(UUID uuid) {
        return bedrockPlayers.contains(uuid);
    }
}
