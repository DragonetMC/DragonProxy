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
    @JsonProperty("SkinGeometry")
    private byte[] skinGeometry; // deserialized
    @JsonProperty("SkinGeometryName")
    private String skinGeometryName;
    @JsonProperty("SkinId")
    private String skinId;
    @JsonProperty("ThirdPartyName")
    private String thirdPartyName;
    @JsonProperty("UIProfile")
    private int uiProfile;
}
