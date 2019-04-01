package org.dragonet.proxy.network.session.data;

import com.nukkitx.protocol.bedrock.session.data.AuthData;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthDataImpl implements AuthData {
    private final String displayName;
    private final UUID identity;
    private final String xuid;

    public AuthDataImpl(String displayName, String identity, String xuid) {
        this.displayName = displayName;
        this.identity = UUID.fromString(identity);
        this.xuid = xuid;
    }
}
