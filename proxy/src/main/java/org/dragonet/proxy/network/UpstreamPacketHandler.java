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
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.data.AuthDataImpl;
import org.dragonet.proxy.remote.RemoteServer;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Respresents the connection between the mcpe client and the proxy.
 */
@Log4j2
@RequiredArgsConstructor
public class UpstreamPacketHandler implements BedrockPacketHandler {

    private final DragonProxy proxy;
    private final BedrockSession<ProxySession> upstream;

    @Override
    public boolean handle(LoginPacket packet) {
        // Check for supported protocol
        int index = Arrays.binarySearch(DragonProxy.BEDROCK_SUPPORTED_PROTOCOLS, packet.getProtocolVersion());
        if (index < 0) {
            upstream.disconnect();
            return true;
        }
        upstream.setPacketCodec(DragonProxy.BEDROCK_SUPPORTED_CODECS[index]);

        try {
            // Get chain data that contains identity info
            JSONObject chainData = (JSONObject) JSONValue.parse(packet.getChainData().array());
            JSONArray chainArray = (JSONArray) chainData.get("chain");

            Object identityObject = chainArray.get(chainArray.size() - 1);

            JWSObject identity = JWSObject.parse((String) identityObject);
            JSONObject extraData = (JSONObject) identity.getPayload().toJSONObject().get("extraData");

            this.upstream.setAuthData(new AuthDataImpl(
                extraData.getAsString("displayName"),
                extraData.getAsString("identity"),
                extraData.getAsString("XUID")
            ));
        } catch (ParseException | ClassCastException | NullPointerException e) {
            // Invalid chain data
            this.upstream.disconnect();
            return true;
        }

        // Tell the Bedrock client login was successful.
        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        upstream.sendPacketImmediately(playStatus);

        // Start Resource pack handshake
        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        upstream.sendPacketImmediately(resourcePacksInfo);
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        switch (packet.getStatus()) {
            case COMPLETED:
            case HAVE_ALL_PACKS:
                // Start connecting to remote server
                RemoteServer remoteServer = new RemoteServer("local", proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort());
                upstream.setPlayer(new ProxySession(proxy, upstream, remoteServer));
                break;
            default:
                // Anything else shouldn't happen so disconnect
                upstream.disconnect("disconnectionScreen.resourcePack");
                break;
        }
        log.info("{} connected", upstream.getAuthData().getDisplayName());
        return true;
    }
}
