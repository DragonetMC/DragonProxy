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
package org.dragonet.plugin.plugin.dpaddon;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.dragonet.common.utilities.BinaryStream;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DPAddonBungee extends Plugin implements Listener {

    public final Set<ProxiedPlayer> recognisedBedrockPlayers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPlayerConnect(PluginMessageEvent e) {
        if(!e.getTag().equals("DragonProxy")) return;
        if(ProxiedPlayer.class.isAssignableFrom(e.getSender().getClass())) {
            BinaryStream bis = new BinaryStream(e.getData());
            String command = bis.getString();

            if(command.equals("Notification")) {
                recognisedBedrockPlayers.add((ProxiedPlayer) e.getSender());
                e.setCancelled(true); // we block the message
            }
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent e) {
        if(!recognisedBedrockPlayers.contains(e.getPlayer())) return;
        // forward the DragonProxy Notification message!
        getProxy().getScheduler().schedule(this, () -> {
            BinaryStream bis = new BinaryStream();
            bis.putString("Notification");
            e.getPlayer().sendData("DragonProxy", bis.getBuffer());
        }, 2000L, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent e) {
        if(!recognisedBedrockPlayers.contains(e.getPlayer())) return;
        // We don't know that another server supports forwarding or not so we disable forwarding for now!
        BinaryStream bis = new BinaryStream();
        bis.putString("PacketFoward");
        bis.putBoolean(false);
        e.getPlayer().sendData("DragonProxy", bis.getBuffer());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        if(recognisedBedrockPlayers.contains(e.getPlayer())) {
            recognisedBedrockPlayers.remove(e.getPlayer());
        }
    }
}
