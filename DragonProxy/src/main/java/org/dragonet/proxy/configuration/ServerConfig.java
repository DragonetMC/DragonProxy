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

import lombok.Getter;
import lombok.Setter;

public class ServerConfig {

    @Getter @Setter
    private String lang = "default";
    
    @Getter @Setter
    private String udp_bind_ip = "0.0.0.0";

    @Getter @Setter
    private int udp_bind_port = 19132;
    
    @Getter @Setter
    private String motd = "&aServer by DragonProxy";
    
    @Getter @Setter
    private String default_server = "NONE";
    
    @Getter @Setter
    private Map<String, RemoteServer> remote_servers;
    
    @Getter @Setter
    private String mode = "cls";
    
    @Getter @Setter
    private String command_prefix = "/";
    
    @Getter @Setter
    private int max_players = -1;
    
    @Getter @Setter
    private boolean log_console = true;
    
    @Getter @Setter
    private int thread_pool_size;
    
    @Getter @Setter
    private boolean acceptPCClients;

}
