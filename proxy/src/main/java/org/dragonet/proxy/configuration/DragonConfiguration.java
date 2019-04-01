/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class DragonConfiguration {

    private String locale = "EN";

    @JsonProperty("bind-address")
    private String bindAddress;

    @JsonProperty("bind-port")
    private int bindPort;

    private String motd;
    private String motd2;

    @JsonProperty("max-players")
    private int maxPlayers;

    @JsonProperty("remote-address")
    private String remoteAddress;

    @JsonProperty("remote-port")
    private int remotePort;
}
