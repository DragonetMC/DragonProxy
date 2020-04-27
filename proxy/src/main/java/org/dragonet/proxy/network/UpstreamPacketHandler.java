/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JWSObject;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.PlayerPermission;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.hybrid.HybridMessageHandler;
import org.dragonet.proxy.network.hybrid.messages.FormResponseMessage;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
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
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents the connection between the bedrock client and the proxy.
 * Most of the LoginPacket code is from the NukkitX project.
 */
@Log4j2
public class UpstreamPacketHandler implements BedrockPacketHandler {
    private DragonProxy proxy;
    private ProxySession session;

    public UpstreamPacketHandler(DragonProxy proxy, ProxySession session) {
        this.proxy = proxy;
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        // Check for supported protocol
        int index = Arrays.binarySearch(DragonProxy.BEDROCK_SUPPORTED_PROTOCOLS, packet.getProtocolVersion());
        if (index < 0) {
            // Set a codec so we can disconnect them
            session.getBedrockSession().setPacketCodec(DragonProxy.BEDROCK_CODEC);

            // We send a start game packet and play status so the client doesn't get stuck on loading resources
            session.sendFakeStartGame(true);
            session.getBedrockSession().disconnect(TextFormat.GOLD + "Unsupported game version.\n" + TextFormat.WHITE + "Please use 1.14.0");
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
        session.sendPacketImmediately(playStatus);

        // Start Resource pack handshake
        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        session.sendPacketImmediately(resourcePacksInfo);
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        switch (packet.getStatus()) {
            case COMPLETED:
                session.handleJoin();

                log.info("{} connected", session.getAuthData().getDisplayName());
                break;
            case HAVE_ALL_PACKS:
                ResourcePackStackPacket stack = new ResourcePackStackPacket();
                stack.setExperimental(false);
                stack.setForcedToAccept(false);
                stack.setGameVersion(DragonProxy.BEDROCK_CODEC.getMinecraftVersion());
                session.sendPacketImmediately(stack);
                break;
            default:
                // Anything else shouldn't happen so disconnect
                session.getBedrockSession().disconnect("disconnectionScreen.resourcePack");
                return false;
        }

        return true;
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        if(session.getDataCache().get("auth_state") == AuthState.AUTHENTICATING) {
            session.sendLoginForm(); // TODO: remove
            return true;
        }
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(ModalFormResponsePacket packet) {
        // Hybrid stuff
        HybridMessageHandler hybridHandler = session.getHybridMessageHandler();
        if(hybridHandler != null && hybridHandler.getFormIdMap().containsKey(packet.getFormId())) {
            session.sendHybridMessage(new FormResponseMessage(hybridHandler.getFormIdMap().get(packet.getFormId()), packet.getFormData()));
            hybridHandler.getFormIdMap().remove(packet.getFormId());
            return true;
        }

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

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        session.spawn(packet.getRuntimeEntityId());
        return true;
    }

    @Override
    public boolean handle(SetPlayerGameTypePacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(SetDefaultGameTypePacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(SetDifficultyPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(SettingsCommandPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
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

    @Override
    public boolean handle(RespawnPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(MobEquipmentPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(CommandBlockUpdatePacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }

    @Override
    public boolean handle(BlockEntityDataPacket packet) {
        PacketTranslatorRegistry.BEDROCK_TO_JAVA.translate(session, packet);
        return true;
    }
}
