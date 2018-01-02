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

import java.util.Map;

public class ServerConfig {

    public String lang = "default";
    public String udp_bind_ip = "0.0.0.0";
    public int udp_bind_port = 19132;
    public String motd = "&aServer by DragonProxy";
    public String default_server = "NONE";
    public Map<String, RemoteServer> remote_servers;
    public String mode = "cls";
    public String command_prefix = "/";
    public int max_players = -1;
    public boolean log_console = true;
    public boolean log_debug = false;
    public boolean authenticate_players = true;
    public int thread_pool_size;

    public ServerConfig() {

    }
}
