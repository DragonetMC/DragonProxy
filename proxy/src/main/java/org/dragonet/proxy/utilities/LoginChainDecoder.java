package org.dragonet.proxy.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import sul.protocol.bedrock137.types.LoginBody;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 2017/9/12.
 */
public class LoginChainDecoder {

    private final LoginBody body;

    public String username;
    public UUID clientUniqueId;

    public JsonObject clientData;

    public LoginChainDecoder(LoginBody body) {
        this.body = body;
    }

    /**
     * decode the chain data in Login packet for MCPE
     * Note: the credit of this function goes to Nukkit development team
     */
    public void decode() {
        // chain
        Map<String, List<String>> map = new Gson().fromJson(new String(body.chain),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
        List<String> chains = map.get("chain");
        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUniqueId = UUID.fromString(extra.get("identity").getAsString());
            }
        }

        // client data
        clientData = decodeToken(new String(body.clientData, StandardCharsets.UTF_8));
    }

    /**
     * Note: the credit of this function goes to Nukkit development team
     * @param token
     * @return
     */
    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1].replace("-_", "+/")), StandardCharsets.UTF_8), JsonObject.class);
    }
}
