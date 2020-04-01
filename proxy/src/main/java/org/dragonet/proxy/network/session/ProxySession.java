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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.google.gson.JsonObject;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.tag.CompoundTag;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.PlayerSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.packet.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.form.CustomForm;
import org.dragonet.proxy.form.components.InputComponent;
import org.dragonet.proxy.form.components.LabelComponent;
import org.dragonet.proxy.network.session.cache.*;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.session.data.AuthData;
import org.dragonet.proxy.network.session.data.AuthState;
import org.dragonet.proxy.network.session.data.ClientData;
import org.dragonet.proxy.network.translator.PacketTranslatorRegistry;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;
import org.dragonet.proxy.remote.RemoteServer;
import org.dragonet.proxy.util.PaletteManager;
import org.dragonet.proxy.util.SkinUtils;
import org.dragonet.proxy.util.TextFormat;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.dragonet.proxy.network.translator.java.PCJoinGameTranslator.EMPTY_LEVEL_CHUNK_DATA;

/**
 * Represents a bedrock player session.
 */
@Data
@Log4j2
public class ProxySession implements PlayerSession {
    private final DragonProxy proxy;
    private RemoteServer remoteServer;
    private MinecraftProtocol protocol;
    private BedrockServerSession bedrockSession;
    private Client downstream;
    private volatile boolean closed;

    private String username;

    private Map<String, Object> dataCache = new HashMap<>();
    private Map<Integer, CompletableFuture> formCache = new HashMap<>();
    private Map<String, CompletableFuture> futureMap = new HashMap<>(); // TODO

    private AtomicInteger formIdCounter = new AtomicInteger();

    private EntityCache entityCache = new EntityCache();
    private WindowCache windowCache = new WindowCache();
    private ChunkCache chunkCache = new ChunkCache();
    private WorldCache worldCache = new WorldCache();
    private PlayerListCache playerListCache = new PlayerListCache();

    private CachedPlayer cachedEntity;

    private AuthData authData;
    private ClientData clientData;

    private int renderDistance = 4;

    public ProxySession(DragonProxy proxy, BedrockServerSession bedrockSession) {
        this.proxy = proxy;
        this.bedrockSession = bedrockSession;
        this.bedrockSession.setLogging(true);

        dataCache.put("auth_state", AuthState.NONE);

        // Disconnect the player from the remote server when they disconnect
        bedrockSession.addDisconnectHandler((reason) -> {
            if (downstream != null && downstream.getSession() != null) {
                if (cachedEntity != null) {
                    cachedEntity.destroy(this);
                }
                downstream.getSession().disconnect(reason.name());
            }
        });
    }

    /**
     * Connect to a remote server.
     *
     * @param server the server to connect to
     */
    public void connect(RemoteServer server) {
        if (protocol == null) {
            protocol = new MinecraftProtocol(authData.getDisplayName());
        }
        downstream = new Client(server.getAddress(), server.getPort(), protocol, new TcpSessionFactory());
        downstream.getSession().addListener(new SessionAdapter() {

            @Override
            public void connected(ConnectedEvent event) {
                log.info("Player connected to remote " + server.getAddress());
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
                log.info("Player disconnected from remote. Reason: " + event.getReason());
                bedrockSession.disconnect(event.getReason());
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                try {
                    //log.info("Packet received from remote: " + event.getPacket().getClass().getSimpleName());
                    PacketTranslatorRegistry.JAVA_TO_BEDROCK.translate(ProxySession.this, event.getPacket());
                } catch (Exception e) {
                    log.throwing(e);
                }
            }
        });

        downstream.getSession().connect();
        remoteServer = server;
    }

    /**
     * Authenticate the user with Mojang account services.
     * This method is only called if `auth-mode` is set to `online`.
     *
     * @param email    the mojang account email
     * @param password the mojang account password
     */
    public void authenticate(String email, String password) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                protocol = new MinecraftProtocol(email, password);
            } catch (RequestException e) {
                log.warn("Failed to authenticate player: " + e.getMessage());
                sendMessage(TextFormat.RED + e.getMessage());
            }

            sendMessage(TextFormat.GREEN + "Login successful! Joining server...");

            if (!username.equals(protocol.getProfile().getName())) {
                username = protocol.getProfile().getName();
                sendMessage(TextFormat.AQUA + "You username was changed to " + TextFormat.DARK_AQUA + username + TextFormat.AQUA + " like your Mojang account username");
            }

            // Empty line to seperate DragonProxy messages from server messages
            sendMessage(" ");

            // Start connecting to remote server
            RemoteServer remoteServer = new RemoteServer("local", proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort());
            connect(remoteServer);

            // Enable coordinates now
            GameRulesChangedPacket gameRulesChangedPacket = new GameRulesChangedPacket();
            gameRulesChangedPacket.getGameRules().add(new GameRuleData<>("showcoordinates", true));
            bedrockSession.sendPacket(gameRulesChangedPacket);

            dataCache.put("auth_state", AuthState.AUTHENTICATED);

            log.info("Player " + authData.getDisplayName() + " has been authenticated");
        });
    }

    /**
     * Fetch statistics from the remote server.
     *
     * @return a future that completes if the server responds to the stats request
     */
    public CompletableFuture<Map<Statistic, Integer>> fetchStatistics() {
        CompletableFuture<Map<Statistic, Integer>> future = new CompletableFuture<>();

        // TODO: this can overwrite the previous future even if its not yet been completed
        // We may want to add better handling for this
        futureMap.put("stats", future);

        ClientRequestPacket packet = new ClientRequestPacket(ClientRequest.STATS);
        sendRemotePacket(packet);
        return future;
    }

    /**
     * Display a form that allows the player to enter their Mojang account credentials.
     * This method is only called if `auth-mode` is set to `online`.
     */
    public void sendLoginForm() {
        CustomForm form = new CustomForm(TextFormat.BLUE + "Login to Mojang account")
            .addComponent(new LabelComponent("DragonProxy"))
            .addComponent(new LabelComponent(TextFormat.GREEN + "Please enter your Mojang account credentials to authenticate"))
            .addComponent(new InputComponent(TextFormat.AQUA + "Email", "steve@example.com"))
            .addComponent(new InputComponent(TextFormat.AQUA + "Password", "123456"));

        form.send(this).whenComplete((data, throwable) -> {
            if (dataCache.get("auth_state") == AuthState.AUTHENTICATED) {
                return; // If multiple forms have been sent to the client, allow the player to actually close them
            }
            if (data == null) {
                sendLoginForm();
                return;
            }

            String email = data.get(2).getAsString();
            String password = data.get(3).getAsString();

            if (email == null || password == null) {
                // This never seems to be fired? Im guessing if one field is null the entire response is null?
                // Anyway, its here just in case
                sendMessage(TextFormat.RED + "Please fill in all the required fields. Move to show the form again.");
                return;
            }

            authenticate(email, password);
        });
    }

    /**
     * Adds the player to the player list
     * @param entityId
     */
    public void spawn(long entityId) {
        PlayerListPacket.Entry entry = new PlayerListPacket.Entry(authData.getIdentity());
        entry.setEntityId(entityId);
        entry.setName(authData.getDisplayName());

        SerializedSkin skin = SerializedSkin.of(
            clientData.getSkinId(),
            ImageData.of(clientData.getSkinImageWidth(), clientData.getSkinImageHeight(), clientData.getSkinData()),
            ImageData.of(clientData.getCapeData()),
            clientData.getSkinGeometryName(),
            new String(clientData.getSkinGeometry(), StandardCharsets.UTF_8),
            false);

        entry.setXuid(authData.getXuid() == null ? "" : authData.getXuid());
        entry.setPlatformChatId("");
        entry.setSkin(skin);

        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.Action.ADD);
        playerListPacket.getEntries().add(entry);

        bedrockSession.sendPacket(playerListPacket);

        SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
        entityDataPacket.setRuntimeEntityId(entityId);
        entityDataPacket.getMetadata().putAll(cachedEntity.getMetadata());
        bedrockSession.sendPacket(entityDataPacket);

        log.warn("SPAWN PLAYER");
    }

    public void sendFakeStartGame() {
        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.setUniqueEntityId(1);
        startGamePacket.setRuntimeEntityId(1);
        startGamePacket.setPlayerGamemode(0);
        startGamePacket.setPlayerPosition(Vector3f.from(-23, 73, 0)); // Hypixel bedwars lobby spawn
        startGamePacket.setRotation(Vector2f.ZERO);

        startGamePacket.setSeed(1111);
        startGamePacket.setDimensionId(0);
        startGamePacket.setGeneratorId(1);
        startGamePacket.setLevelGamemode(0);
        startGamePacket.setDifficulty(0);
        startGamePacket.setDefaultSpawn(Vector3i.ZERO);
        startGamePacket.setAchievementsDisabled(true);
        startGamePacket.setTime(0);
        startGamePacket.setEduEditionOffers(0);
        startGamePacket.setEduFeaturesEnabled(false);
        startGamePacket.setRainLevel(0);
        startGamePacket.setLightningLevel(0);
        startGamePacket.setMultiplayerGame(true);
        startGamePacket.setBroadcastingToLan(true);
        startGamePacket.getGamerules().add((new GameRuleData<>("showcoordinates", true)));
        startGamePacket.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
        startGamePacket.setXblBroadcastMode(GamePublishSetting.PUBLIC);
        startGamePacket.setCommandsEnabled(true);
        startGamePacket.setTexturePacksRequired(false);
        startGamePacket.setBonusChestEnabled(false);
        startGamePacket.setStartingWithMap(false);
        startGamePacket.setTrustingPlayers(true);
        startGamePacket.setDefaultPlayerPermission(PlayerPermission.MEMBER);
        startGamePacket.setServerChunkTickRange(4);
        startGamePacket.setBehaviorPackLocked(false);
        startGamePacket.setResourcePackLocked(false);
        startGamePacket.setFromLockedWorldTemplate(false);
        startGamePacket.setUsingMsaGamertagsOnly(false);
        startGamePacket.setFromWorldTemplate(false);
        startGamePacket.setWorldTemplateOptionLocked(false);

        startGamePacket.setLevelId("DragonProxy " + proxy.getVersion());
        startGamePacket.setWorldName("world");
        startGamePacket.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
        //startGamePacket.setCurrentTick(0);
        startGamePacket.setEnchantmentSeed(0);
        startGamePacket.setMultiplayerCorrelationId("");

        //startGamePacket.setMovementServerAuthoritative(false);
        startGamePacket.setVanillaVersion(DragonProxy.BEDROCK_CODEC.getMinecraftVersion());

        startGamePacket.setBlockPalette(BlockTranslator.BLOCK_PALETTE);
        startGamePacket.setItemEntries(ItemTranslator.ITEM_PALETTE);
        bedrockSession.sendPacketImmediately(startGamePacket);

        BiomeDefinitionListPacket biomeDefinitionListPacket = new BiomeDefinitionListPacket();
        biomeDefinitionListPacket.setTag(PaletteManager.BIOME_ENTRIES);
        sendPacket(biomeDefinitionListPacket);

        AvailableEntityIdentifiersPacket entityPacket = new AvailableEntityIdentifiersPacket();
        entityPacket.setTag(CompoundTag.EMPTY);
        sendPacket(entityPacket);

        // Spawn
        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        sendPacket(playStatusPacket);

        cachedEntity = entityCache.newPlayer(1, new GameProfile(getAuthData().getIdentity(), getAuthData().getDisplayName()));
    }

    /**
     * Set a skin for the specified player.
     *
     * @param playerId the target player uuid
     * @param skinData the skin data, an rgba byte array
     */
    public void setPlayerSkin(UUID playerId, byte[] skinData) {
        GameProfile profile = playerListCache.getPlayerInfo().get(playerId).getProfile();

        // Remove the player from the player list
        PlayerListPacket removePacket = new PlayerListPacket();
        removePacket.setAction(PlayerListPacket.Action.REMOVE);
        removePacket.getEntries().add(new PlayerListPacket.Entry(playerId));
        sendPacket(removePacket);

        // Add them back to the player list with a new skin
        PlayerListPacket addPacket = new PlayerListPacket();
        addPacket.setAction(PlayerListPacket.Action.ADD);

        SerializedSkin skin = SerializedSkin.of(
            profile.getIdAsString(),
            ImageData.of(SkinUtils.STEVE_SKIN_DATA),
            ImageData.EMPTY,
            SkinUtils.getLegacyGeometryName("geometry.humanoid"),
            "",
            false);

        PlayerListPacket.Entry entry = new PlayerListPacket.Entry(playerId);
        entry.setEntityId(1); // TODO
        entry.setName(profile.getName());
        entry.setSkin(skin);
        entry.setXuid("");
        entry.setPlatformChatId("");

        addPacket.getEntries().add(entry);
        sendPacket(addPacket); // TODO

        // TODO: ideally we would use PlayerSkinPacket, but that crashes...
        // See below
    }

    /**
     * Currently used for setting our own skin, however hopefully it can be used to
     * set other players' skins in the future instead of using the player list hack
     *
     * @param playerId the target player uuid
     * @param skinData the skin data, an rgba byte array
     */
    public void setPlayerSkin2(UUID playerId, byte[] skinData) {
        PlayerSkinPacket playerSkinPacket = new PlayerSkinPacket();
        playerSkinPacket.setUuid(playerId);

        SerializedSkin skin = SerializedSkin.of(
            playerId.toString(),
            ImageData.of(skinData),
            ImageData.EMPTY,
            SkinUtils.getLegacyGeometryName("geometry.humanoid"),
            new String(clientData.getSkinGeometry(), StandardCharsets.UTF_8),
            false);

        playerSkinPacket.setSkin(skin);
        sendPacket(playerSkinPacket);
    }

    public void sendMessage(String text) {
        sendMessage(text, TextPacket.Type.RAW);
    }

    /**
     * Send a message to the player.
     *
     * @param text
     * @param type
     */
    public void sendMessage(String text, TextPacket.Type type) {
        TextPacket packet = new TextPacket();
        packet.setType(type);
        packet.setNeedsTranslation(false);
        packet.setXuid("");
        packet.setSourceName("");
        packet.setPlatformChatId("");
        packet.setMessage(text);

        bedrockSession.sendPacket(packet);
    }

    public RemoteServer getRemoteServer() {
        return remoteServer;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        disconnect("Closed");
    }

    /**
     * Disconnect the player from the proxy and the remote server.
     *
     * @param reason the reason show on the disconnection screen and sent
     *               to the remote server if the player is connected to it
     */
    public void disconnect(String reason) {
        if (!isClosed()) {
            if (downstream != null) {
                downstream.getSession().disconnect(reason);
            }
            bedrockSession.disconnect(reason, false);
        }
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectReason disconnectReason) {
        disconnect("Disconnect");
    }

    @Override
    public void onDisconnect(@Nonnull String s) {
        disconnect("Disconnect");
    }

    /**
     * Queue a packet to be sent to player.
     *
     * @param packet the bedrock packet from the NukkitX protocol lib
     */
    public void sendPacket(BedrockPacket packet) {
        if (bedrockSession != null && !bedrockSession.isClosed()) {
            bedrockSession.sendPacket(packet);
        }
    }

    /**
     * Send a packet immediately to the player.
     *
     * @param packet the bedrock packet from the NukkitX protocol lib
     */
    public void sendPacketImmediately(BedrockPacket packet) {
        if (bedrockSession != null && !bedrockSession.isClosed()) {
            bedrockSession.sendPacketImmediately(packet);
        }
    }

    /**
     * Send a packet to the remote server.
     *
     * @param packet the java edition packet from MCProtocolLib
     */
    public void sendRemotePacket(Packet packet) {
        if (downstream != null && downstream.getSession() != null && protocol.getSubProtocol().equals(SubProtocol.GAME)) {
            downstream.getSession().send(packet);
        }
    }
}
