/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import org.dragonet.proxy.remote.RemoteAuthType;

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

    @JsonProperty("remote-auth")
    private RemoteAuthType remoteAuthType;

    @JsonProperty("xbox-auth")
    private boolean xboxAuth;

    @JsonProperty("ping-passthrough")
    private boolean pingPassthrough;

    @JsonProperty("fetch-player-skins")
    private boolean fetchPlayerSkins;

    @JsonProperty("thread-pool-size")
    private int threadPoolSize;
}
