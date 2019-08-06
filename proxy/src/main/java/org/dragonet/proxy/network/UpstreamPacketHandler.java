/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JWSObject;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;

import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
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
import org.dragonet.proxy.network.session.data.ClientData;
import org.dragonet.proxy.network.translator.PacketTranslatorRegistry;
import org.dragonet.proxy.remote.RemoteAuthType;
import org.dragonet.proxy.remote.RemoteServer;
import org.dragonet.proxy.util.BedrockLoginUtils;
import org.dragonet.proxy.util.TextFormat;

import java.io.IOException;
import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Respresents the connection between the mcpe client and the proxy.
 * Most of the LoginPacket code is from the NukkitX project.
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

        JsonNode certData;
        try {
            certData = DragonProxy.JSON_MAPPER.readTree(packet.getChainData().toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException("Certificate JSON could not be read");
        }

        JsonNode certChainData = certData.get("chain");
        if (certChainData.getNodeType() != JsonNodeType.ARRAY) {
            throw new RuntimeException("Certificate data is not valid");
        }

        boolean validChain;
        try {
            validChain = BedrockLoginUtils.validateChainData(certChainData);

            JWSObject jwt = JWSObject.parse(certChainData.get(certChainData.size() - 1).asText());
            JsonNode payload = DragonProxy.JSON_MAPPER.readTree(jwt.getPayload().toBytes());

            if (payload.get("extraData").getNodeType() != JsonNodeType.OBJECT) {
                throw new RuntimeException("AuthData was not found!");
            }

            JSONObject extraData = (JSONObject) jwt.getPayload().toJSONObject().get("extraData");

            session.setAuthData(DragonProxy.JSON_MAPPER.convertValue(extraData, AuthData.class));

            if (payload.get("identityPublicKey").getNodeType() != JsonNodeType.STRING) {
                throw new RuntimeException("Identity Public Key was not found!");
            }

            if(!validChain) {
                if(proxy.getConfiguration().isXboxAuth()) {
                    session.disconnect("You must be authenticated with xbox live");
                    return true;
                }

                session.getAuthData().setXuid(null); // TODO: ideally the class should be immutable
            }

            ECPublicKey identityPublicKey = EncryptionUtils.generateKey(payload.get("identityPublicKey").textValue());
            JWSObject clientJwt = JWSObject.parse(packet.getSkinData().toString());
            EncryptionUtils.verifyJwt(clientJwt, identityPublicKey);

            JsonNode clientPayload = DragonProxy.JSON_MAPPER.readTree(clientJwt.getPayload().toBytes());
            session.setClientData(DragonProxy.JSON_MAPPER.convertValue(clientPayload, ClientData.class));

            session.setUsername(session.getAuthData().getDisplayName());

            if (EncryptionUtils.canUseEncryption()) {
                //BedrockLoginUtils.startEncryptionHandshake(session, identityPublicKey);
            }
        } catch (Exception ex) {
            session.disconnect("disconnectionScreen.internalError.cantConnect");
            throw new RuntimeException("Unable to complete login", ex);
        }

        // Tell the Bedrock client login was successful
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
            return true;
        }
        //PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
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

    @Override
    public boolean handle(PlayerActionPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(InventoryTransactionPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

}
