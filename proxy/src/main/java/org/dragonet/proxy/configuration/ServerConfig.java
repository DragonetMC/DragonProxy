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

public class ServerConfig {

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
    public boolean log_debug = false;
    public boolean authenticate_players = true;
    public int thread_pool_size = 8;
    public Boolean disable_packet_events = true;
    public boolean ping_passthrough = false;

}
