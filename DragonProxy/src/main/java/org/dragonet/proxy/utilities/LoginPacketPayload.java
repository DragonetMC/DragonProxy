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

    private void decodeSkinData(Buffer buf) {
    	try {
    		JsonObject skinToken = decodeToken(new String(buf.readBytes(buf.readLittleEndianInt())));
    		String skinId = null;
    		if (skinToken.has("ClientRandomId")) this.clientId = skinToken.get("ClientRandomId").getAsLong();
    		if (skinToken.has("ServerAddress")) this.serverAddress = skinToken.get("ServerAddress").getAsString();
    		if (skinToken.has("SkinId")) skinId = skinToken.get("SkinId").getAsString();
    		if (skinToken.has("SkinData")) this.skin = new Skin(skinId, Base64.getDecoder().decode(skinToken.get("SkinData").getAsString()));
    	} catch (NegativeArraySizeException e)	{
    		this.clientId = System.currentTimeMillis();
    		this.serverAddress = "0.0.0.0";
    		this.skin = new Skin("Standard_Steve", Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFDUlEQVR42u2a20sUURzH97G0LKMotPuWbVpslj1olJXdjCgyisowsSjzgrB0gSKyC5UF1ZNQWEEQSBQ9dHsIe+zJ/+nXfM/sb/rN4ZwZ96LOrnPgyxzP/M7Z+X7OZc96JpEISfWrFhK0YcU8knlozeJKunE4HahEqSc2nF6zSEkCgGCyb+82enyqybtCZQWAzdfVVFgBJJNJn1BWFgC49/VpwGVlD0CaxQiA5HSYEwBM5sMAdKTqygcAG9+8coHKY/XXAZhUNgDYuBSPjJL/GkzVVhAEU5tqK5XZ7cnFtHWtq/TahdSw2l0HUisr1UKIWJQBAMehDuqiDdzndsP2EZECAG1ZXaWMwOCODdXqysLf++uXUGv9MhUHIByDOijjdiSAoH3ErANQD73C7TXXuGOsFj1d4YH4OTJAEy8y9Hd0mCaeZ5z8dfp88zw1bVyiYhCLOg1ZeAqC0ybaDttHRGME1DhDeVWV26u17lRAPr2+mj7dvULfHw2q65fhQRrLXKDfIxkau3ZMCTGIRR3URR5toU38HbaPiMwUcKfBAkoun09PzrbQ2KWD1JJaqswjdeweoR93rirzyCMBCmIQizqoizZkm2H7iOgAcHrMHbbV9KijkUYv7qOn55sdc4fo250e+vUg4329/Xk6QB/6DtOws+dHDGJRB3XRBve+XARt+4hIrAF4UAzbnrY0ve07QW8uHfB+0LzqanMM7qVb+3f69LJrD90/1axiEIs6qIs21BTIToewfcSsA+Bfb2x67OoR1aPPzu2i60fSNHRwCw221Suz0O3jO+jh6V1KyCMGse9721XdN5ePutdsewxS30cwuMjtC860T5JUKpXyKbSByUn7psi5l+juDlZYGh9324GcPKbkycaN3jUSAGxb46IAYPNZzW0AzgiQ5tVnzLUpUDCAbakMQXXrOtX1UMtHn+Q9/X5L4wgl7t37r85OSrx+TYl379SCia9KXjxRpiTjIZTBFOvrV1f8ty2eY/T7XJ81FQAwmA8ASH1ob68r5PnBsxA88/xAMh6SpqW4HRnLBrkOA9Xv5wPAZjAUgOkB+SHxgBgR0qSMh0zmZRsmwDJm1gFg2PMDIC8/nAHIMls8x8GgzOsG5WiaqREgYzDvpTwjLDy8NM15LpexDEA3LepjU8Z64my+8PtDCmUyRr+fFwA2J0eAFYA0AxgSgMmYBMZTwFQnO9RNAEaHOj2DXF5UADmvAToA2ftyxZYA5BqgmZZApDkdAK4mAKo8GzPlr8G8AehzMAyA/i1girUA0HtYB2CaIkUBEHQ/cBHSvwF0AKZFS5M0ZwMQtEaEAmhtbSUoDADH9ff3++QZ4o0I957e+zYAMt6wHkhzpjkuAcgpwNcpA7AZDLsvpwiuOkBvxygA6Bsvb0HlaeKIF2EbADZpGiGzBsA0gnwQHGOhW2snRpbpPexbAB2Z1oicAMQpTnGKU5ziFKc4xSlOcYpTnOIUpzgVmgo+XC324WfJAdDO/+ceADkCpuMFiFKbApEHkOv7BfzfXt+5gpT8V7rpfYJcDz+jAsB233r6yyBsJ0mlBCDofuBJkel4vOwBFPv8fyYAFPJ+wbSf/88UANNRVy4Awo6+Ig2gkCmgA5DHWjoA+X7AlM//owLANkX0w0359od++pvX8fdMAcj3/QJ9iJsAFPQCxHSnQt8vMJ3v2wCYpkhkAOR7vG7q4aCXoMoSgG8hFAuc/grMdAD4B/kHl9da7Ne9AAAAAElFTkSuQmCC"));
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
