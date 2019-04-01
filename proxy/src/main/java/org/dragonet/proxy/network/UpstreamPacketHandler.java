/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network;

import com.nimbusds.jose.JWSObject;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.session.data.AuthDataImpl;
import org.dragonet.proxy.util.RemoteServer;

import java.text.ParseException;

/**
 * Respresents the connection between the mcpe client and the proxy.
 */
@Log4j2
public class UpstreamPacketHandler implements BedrockPacketHandler {

    private BedrockSession<UpstreamSession> session;
    private DragonProxy proxy;

    public UpstreamPacketHandler(BedrockSession<UpstreamSession> session, DragonProxy proxy) {
        this.session = session;
        this.proxy = proxy;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        // TODO: move out of here? idk
        UpstreamSession session = new UpstreamSession(this.session);
        this.session.setPlayer(session);


        try {
            // Get chain data that contains identity info
            JSONObject chainData = (JSONObject) JSONValue.parse(packet.getChainData().array());
            JSONArray chainArray = (JSONArray) chainData.get("chain");

            Object identityObject = chainArray.get(chainArray.size() - 1);

            JWSObject identity = JWSObject.parse((String) identityObject);
            JSONObject extraData = (JSONObject) identity.getPayload().toJSONObject().get("extraData");

            this.session.setAuthData(new AuthDataImpl(
                extraData.getAsString("displayName"),
                extraData.getAsString("identity"),
                extraData.getAsString("XUID")
            ));
        } catch (ParseException | ClassCastException | NullPointerException e) {
            // Invalid chain data
            this.session.disconnect();
            return true;
        }

        session.setRemoteServer(new RemoteServer("local", proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort()));
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        session.getPlayer().postLogin();
        return true;
    }
}
