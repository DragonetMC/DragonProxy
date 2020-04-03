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
package org.dragonet.proxy.network.session.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientData {
    @JsonProperty("CapeData")
    private byte[] capeData; // deserialized
    @JsonProperty("ClientRandomId")
    private long clientRandomId;
    @JsonProperty("CurrentInputMode")
    private int currentInputMode;
    @JsonProperty("DefaultInputMode")
    private int defaultInputMode;
    @JsonProperty("DeviceId")
    private String deviceId;
    @JsonProperty("DeviceModel")
    private String deviceModel;
    @JsonProperty("DeviceOS")
    private DeviceOS deviceOs;
    @JsonProperty("GameVersion")
    private String gameVersion;
    @JsonProperty("GuiScale")
    private int guiScale;
    @JsonProperty("LanguageCode")
    private String languageCode;
    @JsonProperty("PlatformOfflineId")
    private String platformOfflineId;
    @JsonProperty("PlatformOnlineId")
    private String platformOnlineId;
    @JsonProperty("PremiumSkin")
    private boolean premiumSkin;
    @JsonProperty("SelfSignedId")
    private UUID selfSignedId;
    @JsonProperty("ServerAddress")
    private String serverAddress;
    @JsonProperty("SkinData")
    private byte[] skinData; // deserialized
    @JsonProperty("SkinGeometryData")
    private byte[] skinGeometry; // deserialized
    @JsonProperty("SkinResourcePatch")
    private String skinGeometryName;
    @JsonProperty("SkinImageHeight")
    private int skinImageHeight;
    @JsonProperty("SkinImageWidth")
    private int skinImageWidth;
    @JsonProperty("SkinId")
    private String skinId;
    @JsonProperty("ThirdPartyName")
    private String thirdPartyName;
    @JsonProperty("UIProfile")
    private UIProfile uiProfile;
}
