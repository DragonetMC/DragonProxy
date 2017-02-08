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
package org.dragonet.proxy.utilities;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import sul.protocol.pocket100.play.Login;
import sul.protocol.pocket100.types.Skin;
import sul.utils.Buffer;

/**
 *
 * @author The Nukkit Project
 */
public class LoginPacketPayload {

    @Getter
    private String username;

    @Getter
    private UUID clientUUID;

    @Getter
    private long clientId;

    @Getter
    private String serverAddress;

    @Getter
    private Skin skin;

    @Getter
    private String identityPublicKey;

    private LoginPacketPayload() {
    }

    public static LoginPacketPayload decode(byte[] body) {
        LoginPacketPayload data = new LoginPacketPayload();
        byte[] str;
        try {
            str = Zlib.inflate(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Buffer buffer = new Buffer();
        buffer._buffer = str;
        data.decodeChainData(buffer);
        data.decodeSkinData(buffer);
        return data;
    }

    private void decodeChainData(Buffer buffer) {
        Map<String, List<String>> map = new Gson().fromJson(new String(buffer.readBytes(buffer.readLittleEndianInt()), StandardCharsets.UTF_8),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) {
            return;
        }
        List<String> chains = map.get("chain");
        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) {
                continue;
            }
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) {
                    this.username = extra.get("displayName").getAsString();
                }
                if (extra.has("identity")) {
                    this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
                }
            }
            if (chainMap.has("identityPublicKey")) {
                this.identityPublicKey = chainMap.get("identityPublicKey").getAsString();
            }
        }
    }

    private void decodeSkinData(Buffer buffer) {
        JsonObject skinToken = decodeToken(new String(buffer.readBytes(buffer.readLittleEndianInt())));
        String skinId = null;
        if (skinToken.has("ClientRandomId")) {
            this.clientId = skinToken.get("ClientRandomId").getAsLong();
        }
        if (skinToken.has("ServerAddress")) {
            this.serverAddress = skinToken.get("ServerAddress").getAsString();
        }
        if (skinToken.has("SkinId")) {
            skinId = skinToken.get("SkinId").getAsString();
        }
        if (skinToken.has("SkinData")) {
            this.skin = new Skin(skinToken.get("SkinData").getAsString(), skinId.getBytes());
        }
    }

    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) {
            return null;
        }
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }

    private static byte[] encode(LoginPacketPayload loginData) {
        //TODO: Finish this later
        return new byte[0];
    }
}
