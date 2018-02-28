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
import com.google.gson.JsonSyntaxException;
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
        if (session.getProfile().isLoginVerified()) {
            Map<String, String> postData = new HashMap();
            postData.put("chain", session.getProfile().getChainJWT());
            postData.put("token", session.getProfile().getClientDataJWT());
            String rawResult = HTTP.performPostRequest(this.clsServer + "/api/v1/authenticate", postData);
            if (rawResult != null)
                try {
                    JsonObject result = gson.fromJson(rawResult, JsonObject.class);
                    if (result.has("pin")) {
                        session.getDataCache().put("cls_link_server", this.clsServer + "/link");
                        session.getDataCache().put("cls_link_pin", result.get("pin").getAsString());
                        return false;
                    } else if (result.has("success")) {
                        session.getDataCache().put("mojang_clientToken", result.get("clientToken").getAsString());
                        session.getDataCache().put("mojang_accessToken", result.get("accessToken").getAsString());
                        session.getDataCache().put("mojang_uuid", result.get("uuid").getAsString());
                        session.getDataCache().put("mojang_displayName", result.get("displayName").getAsString());
                        return true;
                    }
                } catch (JsonSyntaxException ex) {
                    session.getProxy().getLogger().warning("An error occured on CLS server.");
                    return false;
                }
            else {
                session.getProxy().getLogger().warning("Unable to connect to CLS server.");
                return false;
            }
        }
        session.getProxy().getLogger().warning("Trying to authenticate " + session.getUsername() + " with CLS server but Xbox Live not connected.");
        return false;
    }

    public boolean refresh(UpstreamSession session, String newAccessToken) {
        if (session.getDataCache().containsKey("mojang_clientToken") && session.getDataCache().containsKey("mojang_uuid")) {
            Map<String, String> postData = new HashMap();
            postData.put("uuid", (String) session.getDataCache().get("mojang_uuid"));
            postData.put("accessToken", newAccessToken);
            String rawResult = HTTP.performPostRequest(this.clsServer + "/api/v1/refresh", postData);
            if (rawResult != null)
                try {
                    JsonObject result = gson.fromJson(rawResult, JsonObject.class);
                    if (result.has("success"))
                        return true;
                } catch (JsonSyntaxException ex) {
                    session.getProxy().getLogger().warning("An error occured on CLS server.");
                    return false;
                }
            else {
                session.getProxy().getLogger().warning("Unable to connect to CLS server.");
                return false;
            }
        }
        return false;
    }
}
