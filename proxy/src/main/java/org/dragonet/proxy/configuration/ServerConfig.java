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
package org.dragonet.proxy.configuration;

import org.dragonet.api.configuration.IServerConfig;

public class ServerConfig implements IServerConfig {

    public String lang = "default";
    public String udp_bind_ip = "0.0.0.0";
    public int udp_bind_port = 19132;
    public String proxy_type = "NONE";
    public String proxy_ip = "";
    public int proxy_port = 8080;
    public String motd = "&aServer by DragonProxy";
    public String remote_server_addr = "mc.hypixel.net";
    public int remote_server_port = 25565;
    public String mode = "online";
    public Boolean auto_login = false;
    public String online_username = "myUsername";
    public String online_password = "myPassword";
    public String cls_server = "http://auth.dragonet.org";
    public String command_prefix = "/";
    public int max_players = 10;
    public boolean log_console = false;
    public boolean log_colors = true;
    public boolean authenticate_players = true;
    public int thread_pool_size = 8;
    public Boolean disable_packet_events = true;
    public boolean ping_passthrough = false;
    
    /**
     * @return the lang
     */
    @Override
    public String getLang() {
        return lang;
    }

    /**
     * @return the udp_bind_ip
     */
    @Override
    public String getUdp_bind_ip() {
        return udp_bind_ip;
    }

    /**
     * @return the udp_bind_port
     */
    @Override
    public int getUdp_bind_port() {
        return udp_bind_port;
    }

    /**
     * @return the proxy_type
     */
    @Override
    public String getProxy_type() {
        return proxy_type;
    }

    /**
     * @return the proxy_ip
     */
    @Override
    public String getProxy_ip() {
        return proxy_ip;
    }

    /**
     * @return the proxy_port
     */
    @Override
    public int getProxy_port() {
        return proxy_port;
    }

    /**
     * @return the motd
     */
    @Override
    public String getMotd() {
        return motd;
    }

    /**
     * @return the remote_server_addr
     */
    @Override
    public String getRemote_server_addr() {
        return remote_server_addr;
    }

    /**
     * @return the remote_server_port
     */
    @Override
    public int getRemote_server_port() {
        return remote_server_port;
    }

    /**
     * @return the mode
     */
    @Override
    public String getMode() {
        return mode;
    }

    /**
     * @return the auto_login
     */
    @Override
    public Boolean getAuto_login() {
        return auto_login;
    }

    /**
     * @return the online_username
     */
    @Override
    public String getOnline_username() {
        return online_username;
    }

    /**
     * @return the online_password
     */
    @Override
    public String getOnline_password() {
        return online_password;
    }

    /**
     * @return the cls_server
     */
    @Override
    public String getCls_server() {
        return cls_server;
    }

    /**
     * @return the command_prefix
     */
    @Override
    public String getCommand_prefix() {
        return command_prefix;
    }

    /**
     * @return the max_players
     */
    @Override
    public int getMax_players() {
        return max_players;
    }

    /**
     * @return the log_console
     */
    @Override
    public boolean isLog_console() {
        return log_console;
    }

    /**
     * @return the log_colors
     */
    @Override
    public boolean isLog_colors() {
        return log_colors;
    }

    /**
     * @return the authenticate_players
     */
    @Override
    public boolean isAuthenticate_players() {
        return authenticate_players;
    }

    /**
     * @return the thread_pool_size
     */
    @Override
    public int getThread_pool_size() {
        return thread_pool_size;
    }

    /**
     * @return the disable_packet_events
     */
    @Override
    public Boolean getDisable_packet_events() {
        return disable_packet_events;
    }

    /**
     * @return the ping_passthrough
     */
    @Override
    public boolean isPing_passthrough() {
        return ping_passthrough;
    }

}
