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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.common.utilities.HTTP;
import org.dragonet.proxy.DragonProxy;

/**
 *
 * @author Epic
 */
public class CLSAuthenticationService {

    private static CLSAuthenticationService instance;
    private final Gson gson = new Gson();
    private final String clsServer;

    public static CLSAuthenticationService getInstance() {
        if (instance == null)
            instance = new CLSAuthenticationService();
        return instance;
    }

    private CLSAuthenticationService() {
        this.clsServer = DragonProxy.getInstance().getConfig().cls_server;
    }

    public boolean authenticate(UpstreamSession session) {
        if (session.getProfile().extraData != null)
            if (session.getProfile().extraData.has("XUID") && session.getProfile().extraData.has("identity") && session.getProfile().extraData.has("displayName")) {
                Map<String, String> postData = new HashMap();
                postData.put("XUID", session.getProfile().extraData.get("XUID").getAsString());
                postData.put("identity", session.getProfile().extraData.get("identity").getAsString());
                postData.put("displayName", session.getProfile().extraData.get("displayName").getAsString());
                String rawResult = HTTP.performPostRequest(this.clsServer + "/api/v1/authenticate", postData);
                if (rawResult != null) {
                    JsonObject result = gson.fromJson(rawResult, JsonObject.class);
                     if (result.has("pin")) {
                        session.getDataCache().put("cls_link_server", this.clsServer + "/register.html");
                        session.getDataCache().put("cls_link_pin", result.get("pin").getAsString());
                        return false;
                    } else if (result.has("success")) {
                        session.getDataCache().put("mojang_clientToken", result.get("clientToken").getAsString());
                        session.getDataCache().put("mojang_accessToken", result.get("accessToken").getAsString());
                        session.getDataCache().put("mojang_uuid", result.get("uuid").getAsString());
                        session.getDataCache().put("mojang_displayName", result.get("displayName").getAsString());
                        return true;
                    }
                }
            }
        session.sendChat("Something bad happened, report the problem !");
        return false;
    }

    public boolean refresh(UpstreamSession session, String newAccessToken) {
        if (session.getDataCache().containsKey("mojang_clientToken") && session.getDataCache().containsKey("mojang_uuid")) {
            Map<String, String> postData = new HashMap();
            postData.put("uuid", (String) session.getDataCache().get("mojang_uuid"));
            postData.put("accessToken", newAccessToken);
            String rawResult = HTTP.performPostRequest(this.clsServer + "/api/v1/refresh", postData);
            if (rawResult != null) {
                JsonObject result = gson.fromJson(rawResult, JsonObject.class);
                if (result.has("success")) {
                    return true;
                }
            }
        }
        return false;
    }
}
