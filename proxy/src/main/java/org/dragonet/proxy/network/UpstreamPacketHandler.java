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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JWSObject;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.form.CustomForm;
import org.dragonet.proxy.form.Form;
import org.dragonet.proxy.form.components.InputComponent;
import org.dragonet.proxy.form.components.LabelComponent;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.data.AuthData;
import org.dragonet.proxy.network.session.data.AuthState;
import org.dragonet.proxy.network.translator.PacketTranslatorRegistry;
import org.dragonet.proxy.remote.RemoteAuthType;
import org.dragonet.proxy.remote.RemoteServer;
import org.dragonet.proxy.util.TextFormat;

import java.text.ParseException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Respresents the connection between the mcpe client and the proxy.
 */
@Log4j2
public class UpstreamPacketHandler implements BedrockPacketHandler {

    private DragonProxy proxy;
    private ProxySession session;

    public UpstreamPacketHandler(DragonProxy proxy, BedrockServerSession bedrockSession) {
        this.proxy = proxy;
        this.session = new ProxySession(proxy, bedrockSession);
    }

    @Override
    public boolean handle(LoginPacket packet) {
        // Check for supported protocol
        int index = Arrays.binarySearch(DragonProxy.BEDROCK_SUPPORTED_PROTOCOLS, packet.getProtocolVersion());
        if (index < 0) {
            session.getBedrockSession().disconnect();
            return true;
        }
        session.getBedrockSession().setPacketCodec(DragonProxy.BEDROCK_SUPPORTED_CODECS[index]);

        try {
            // Get chain data that contains identity info
            JSONObject chainData = (JSONObject) JSONValue.parse(packet.getChainData().array());
            JSONArray chainArray = (JSONArray) chainData.get("chain");

            Object identityObject = chainArray.get(chainArray.size() - 1);

            JWSObject identity = JWSObject.parse((String) identityObject);
            JSONObject extraData = (JSONObject) identity.getPayload().toJSONObject().get("extraData");

            session.setAuthData(new AuthData(
                extraData.getAsString("displayName"),
                extraData.getAsString("identity"),
                extraData.getAsString("XUID")
            ));

            session.setUsername(session.getAuthData().getDisplayName());
        } catch (ParseException | ClassCastException | NullPointerException e) {
            // Invalid chain data
            session.getBedrockSession().disconnect();
            return true;
        }

        // Tell the Bedrock client login was successful.
        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        session.getBedrockSession().sendPacketImmediately(playStatus);

        // Start Resource pack handshake
        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        session.getBedrockSession().sendPacketImmediately(resourcePacksInfo);
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        switch (packet.getStatus()) {
            case COMPLETED:
                if(proxy.getConfiguration().getRemoteAuthType() == RemoteAuthType.CREDENTIALS) {
                    session.getDataCache().put("auth_state", AuthState.AUTHENTICATING);
                    session.sendFakeStartGame();
                    session.sendLoginForm();
                    return true;
                }

                // Start connecting to remote server
                RemoteServer remoteServer = new RemoteServer("local", proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort());
                session.connect(remoteServer);
                break;
            case HAVE_ALL_PACKS:
                ResourcePackStackPacket stack = new ResourcePackStackPacket();
                stack.setExperimental(false);
                stack.setForcedToAccept(false);
                session.getBedrockSession().sendPacketImmediately(stack);
                break;
            default:
                // Anything else shouldn't happen so disconnect
                session.getBedrockSession().disconnect("disconnectionScreen.resourcePack");
                return false;
        }

        log.info("{} connected", session.getAuthData().getDisplayName());
        return true;
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        if(session.getDataCache().get("auth_state") == AuthState.AUTHENTICATING) {
            session.sendLoginForm(); // TODO: remove
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(ModalFormResponsePacket packet) {
        if(session.getFormCache().containsKey(packet.getFormId())) {
            CompletableFuture<JsonArray> future = session.getFormCache().get(packet.getFormId());
            JsonElement data = new JsonParser().parse(packet.getFormData());

            if(!data.isJsonArray()) {
                future.complete(null);
                return true;
            }

            future.complete(new JsonParser().parse(packet.getFormData()).getAsJsonArray());
        }
        return true;
    }

    // TODO: a better method
    @Override
    public boolean handle(TextPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(AnimatePacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }
}
