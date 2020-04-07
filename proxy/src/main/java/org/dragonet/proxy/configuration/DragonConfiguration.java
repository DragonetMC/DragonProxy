/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.remote.RemoteAuthType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DragonConfiguration {
    @JsonProperty("config-version")
    private int configVersion;

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

    @JsonProperty("enable-commands")
    private boolean commandsEnabled;

    @JsonProperty("metrics")
    private MetricsConfiguration metrics;

    @JsonProperty("thread-pool-size")
    private int threadPoolSize;

    @Getter
    public static class MetricsConfiguration {
        private boolean enabled;
        @JsonProperty("server-uuid")
        private String serverId;

        @JsonSetter("server-uuid")
        public void setServerId(String uuid) {
            this.serverId = uuid;
        }
    }
}
