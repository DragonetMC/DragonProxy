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
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.packets.*;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.common.data.entity.EntityType;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.PCDownstreamSession;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.common.data.entity.Skin;
import org.dragonet.common.utilities.BinaryStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.ChunkPos;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.common.maths.NukkitMath;

public class PCPlayerPositionRotationPacketTranslator implements IPCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {

        CachedEntity entityPlayer = session.getEntityCache().getClientEntity();
        if (entityPlayer == null) {
            //disconnect (important missing data)
        }

        if (!session.isSpawned()) {
            if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
                session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
                return null;
            }

            ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
            if (!session.getProxy().getAuthMode().equalsIgnoreCase("online")) {
                StartGamePacket ret = new StartGamePacket();
                ret.rtid = entityPlayer.proxyEid;
                ret.eid = entityPlayer.proxyEid;
                ret.dimension = entityPlayer.dimention;
                ret.seed = 0;
                ret.generator = 1;
                ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
                ret.spawnPosition = new BlockPosition((int) packet.getX(), (int) packet.getY(), (int) packet.getZ());
                ret.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset() + 0.1f, (float) packet.getZ());
                ret.yaw = packet.getYaw();
                ret.pitch = packet.getPitch();
                ret.levelId = "";
                ret.worldName = "World";
                ret.commandsEnabled = true;
                ret.defaultPlayerPermission = 2;
                ret.premiumWorldTemplateId = "";
                ret.difficulty = restored.getDifficulty();
                session.sendPacket(ret, true);
            }

            entityPlayer.absoluteMove(packet.getX(), packet.getY() + entityPlayer.peType.getOffset() + 0.1f, packet.getZ(), packet.getYaw(), packet.getPitch());
            session.getChunkCache().sendOrderedChunks();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("DragonProxy");
            ClientPluginMessagePacket clientPluginMessagePacket = new ClientPluginMessagePacket("MC|Brand", out.toByteArray());
            ((PCDownstreamSession) session.getDownstream()).send(clientPluginMessagePacket);

            LoginPacket loginpacket = (LoginPacket) session.getDataCache().remove(CacheKey.PACKET_LOGIN_PACKET);
            String clientLanguage = loginpacket.decoded.clientData.has("LanguageCode") ? loginpacket.decoded.clientData.get("LanguageCode").getAsString() : "en_US";
            session.getDataCache().put(CacheKey.PLAYER_LANGUAGE, clientLanguage);

            ClientSettingsPacket clientSettingsPacket = new ClientSettingsPacket(
                    clientLanguage,
                    (int) session.getDataCache().getOrDefault(CacheKey.PLAYER_REQUESTED_CHUNK_RADIUS, 5),
                    ChatVisibility.FULL,
                    false,
                    new SkinPart[]{},
                    Hand.OFF_HAND);
            ((PCDownstreamSession) session.getDownstream()).send(clientSettingsPacket);

            UpdateAttributesPacket attr = new UpdateAttributesPacket();
            attr.rtid = entityPlayer.proxyEid;
            if (entityPlayer.attributes.isEmpty()) {
                attr.entries = new ArrayList();
                attr.entries.addAll(PEEntityAttribute.getDefault());
            } else
                attr.entries = entityPlayer.attributes.values();
            session.sendPacket(attr, true);

            AdventureSettingsPacket adv = new AdventureSettingsPacket();
            //flags
            adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, restored.getGameMode().equals(GameMode.ADVENTURE));
            //adv.setFlag(AdventureSettingsPacket.NO_PVP, true);
            //adv.setFlag(AdventureSettingsPacket.AUTO_JUMP, true);
            adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, restored.getGameMode().equals(GameMode.CREATIVE) || restored.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.NO_CLIP, restored.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.WORLD_BUILDER, !restored.getGameMode().equals(GameMode.SPECTATOR) || !restored.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.FLYING, restored.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.MUTED, false);
            //custom permission flags (not necessary for now when using LEVEL_PERMISSION setting)
            //adv.setFlag(AdventureSettingsPacket.BUILD_AND_MINE, true);adv.setFlag(AdventureSettingsPacket.BUILD_AND_MINE, true);
            //adv.setFlag(AdventureSettingsPacket.DOORS_AND_SWITCHES, true);
            //adv.setFlag(AdventureSettingsPacket.OPEN_CONTAINERS, true);
            //adv.setFlag(AdventureSettingsPacket.ATTACK_PLAYERS, true);
            //adv.setFlag(AdventureSettingsPacket.ATTACK_MOBS, true);
            //adv.setFlag(AdventureSettingsPacket.OPERATOR, true);
            //adv.setFlag(AdventureSettingsPacket.TELEPORT, true);
            adv.eid = entityPlayer.proxyEid;
            adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;     //TODO update this with server configiration
            adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER; //TODO update this with server configiration
            session.sendPacket(adv, true);

            SetEntityDataPacket entityData = new SetEntityDataPacket();
            entityData.rtid = entityPlayer.proxyEid;
            entityData.meta = EntityMetaData.createDefault();
            session.sendPacket(entityData, true);

            if (restored.getGameMode().equals(GameMode.CREATIVE))
                session.sendCreativeInventory();

            if (session.getProxy().getAuthMode().equalsIgnoreCase("online")) {

                MovePlayerPacket pk = new MovePlayerPacket();
                pk.rtid = entityPlayer.proxyEid;
                pk.mode = MovePlayerPacket.MODE_TELEPORT;
                pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset() + 0.1f, (float) packet.getZ());
                pk.yaw = packet.getYaw();
                pk.pitch = packet.getPitch();
                pk.headYaw = packet.getYaw();

                if (entityPlayer.riding != 0) {
                    CachedEntity vehicle = session.getEntityCache().getByLocalEID(entityPlayer.riding);
                    if (vehicle != null)
                        pk.ridingRuntimeId = vehicle.eid;
                }
                session.sendPacket(pk, true);
            }

            // Notify the server
            BinaryStream bis = new BinaryStream();
            bis.putString("Notification"); // command
            ClientPluginMessagePacket pluginMessage = new ClientPluginMessagePacket("DragonProxy", bis.get());
            session.getDownstream().send(pluginMessage);

            session.setSpawned();

            DragonProxy.getInstance().getLogger().info("Spawning " + session.getUsername() + " in world " + entityPlayer.dimention + " at " + entityPlayer.x + "/" + entityPlayer.y + "/" + entityPlayer.z);

            // send the confirmation
            ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
            ((PCDownstreamSession) session.getDownstream()).send(confirm);

            PlayerListPacket playerListPacket = new PlayerListPacket();
            Set<org.dragonet.protocol.type.PlayerListEntry> peEntries = new HashSet();

            for (CachedEntity entity : session.getEntityCache().getEntities().values()) {
                if (entity.peType == EntityType.PLAYER) {
                    PlayerListEntry playerListEntry = session.getPlayerInfoCache().get(entity.playerUniqueId);

                    org.dragonet.protocol.type.PlayerListEntry peEntry = new org.dragonet.protocol.type.PlayerListEntry();
                    peEntry.uuid = entity.playerUniqueId;
                    peEntry.eid = entity.eid;
                    peEntry.username = playerListEntry.getProfile().getName();
                    peEntry.skin = Skin.DEFAULT_SKIN_STEVE;
                    peEntry.xboxUserId = "null";
                    peEntries.add(peEntry);
                }
                entity.spawn(session);
            }

            playerListPacket.type = PlayerListPacket.TYPE_ADD;
            playerListPacket.entries = peEntries.toArray(new org.dragonet.protocol.type.PlayerListEntry[peEntries.size()]);
            session.sendPacket(playerListPacket);
            entityPlayer.spawned = true;
            return null;
        }

        entityPlayer.absoluteMove(packet.getX(), packet.getY() + entityPlayer.peType.getOffset() + 0.1f, packet.getZ(), packet.getYaw(), packet.getPitch());
        session.getChunkCache().sendOrderedChunks();

        float offset = 0.01f;
        byte mode = MovePlayerPacket.MODE_NORMAL;
        ChunkPos chunk = new ChunkPos(NukkitMath.ceilDouble(packet.getX()) >> 4, NukkitMath.ceilDouble(packet.getZ()) >> 4);
        // check if destination is out of range
        if (!session.getChunkCache().getLoadedChunks().contains(chunk)) {
            mode = MovePlayerPacket.MODE_TELEPORT;
            offset = 0.2f;
//            System.out.println(packet.getX() + " " + packet.getZ());
//            System.out.println("out of range !" + chunk.toString());
            session.getChunkCache().sendOrderedChunks();
//            session.getChunkCache().getDebugGrid();
        }

        MovePlayerPacket pk = new MovePlayerPacket();
        pk.rtid = entityPlayer.proxyEid;
        pk.mode = mode;
        pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset() + offset, (float) packet.getZ());
        pk.yaw = packet.getYaw();
        pk.pitch = packet.getPitch();
        pk.headYaw = packet.getYaw();

        if (entityPlayer.riding != 0) {
            CachedEntity vehicle = session.getEntityCache().getByLocalEID(entityPlayer.riding);
            if (vehicle != null)
                pk.ridingRuntimeId = vehicle.eid;
        }
//        System.out.println("From server " + packet.getX() + " " + packet.getY() + " " + packet.getZ() + " ");
//        System.out.println("Entity position " + entityPlayer.x + " " + (entityPlayer.y - EntityType.PLAYER.getOffset()) + " " + entityPlayer.z + " ");
        session.sendPacket(pk);

        // send the confirmation
        ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
        ((PCDownstreamSession) session.getDownstream()).send(confirm);

        return null;
    }
}
