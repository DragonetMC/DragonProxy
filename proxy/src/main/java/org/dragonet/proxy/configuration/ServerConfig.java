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

    private String lang = "default";
    private String udp_bind_ip = "0.0.0.0";
    private int udp_bind_port = 19132;
    private String proxy_type = "NONE";
    private String proxy_ip = "";
    private int proxy_port = 8080;
    private String motd = "&aServer by DragonProxy";
    private String remote_server_addr = "mc.hypixel.net";
    private int remote_server_port = 25565;
    private String mode = "online";
    private Boolean auto_login = false;
    private String online_username = "myUsername";
    private String online_password = "myPassword";
    private String cls_server = "http://auth.dragonet.org";
    private String command_prefix = "/";
    private int max_players = 10;
    private boolean log_console = false;
    private boolean log_colors = true;
    private boolean log_debug = false;
    private boolean authenticate_players = true;
    private int thread_pool_size = 8;
    private Boolean disable_packet_events = true;
    private boolean ping_passthrough = false;
    
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
     * @return the log_debug
     */
    @Override
    public boolean isLog_debug() {
        return log_debug;
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
