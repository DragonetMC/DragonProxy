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

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.PCDownstreamSession;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.data.entity.meta.type.ByteArrayMeta;
import org.dragonet.proxy.data.entity.meta.type.SlotMeta;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.protocol.packets.*;
import org.dragonet.proxy.protocol.type.Skin;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.Vector3F;

public class PCPlayerPositionRotationPacketTranslator implements IPCPacketTranslator<ServerPlayerPositionRotationPacket> {

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

            ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache()
                    .remove(CacheKey.PACKET_JOIN_GAME_PACKET);
            if (!session.getProxy().getAuthMode().equalsIgnoreCase("online")) {
                StartGamePacket ret = new StartGamePacket();
                ret.rtid = entityPlayer.proxyEid;
                ret.eid = entityPlayer.proxyEid;
                ret.dimension = restored.getDimension();
                ret.seed = 0;
                ret.generator = 1;
                ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
                ret.spawnPosition = new BlockPosition((int) packet.getX(), (int) packet.getY(), (int) packet.getZ());
                ret.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset(),
                        (float) packet.getZ());
                ret.yaw = packet.getYaw();
                ret.pitch = packet.getPitch();
                ret.levelId = "";
                ret.worldName = "World";
                ret.commandsEnabled = true;
                ret.defaultPlayerPermission = 2;
                ret.premiumWorldTemplateId = "";
                session.sendPacket(ret);
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("DragonProxy");
            ClientPluginMessagePacket ClientPluginMessagePacket = new ClientPluginMessagePacket("MC|Brand", out.toByteArray());
            ((PCDownstreamSession) session.getDownstream()).send(ClientPluginMessagePacket);

            LoginPacket loginpacket = (LoginPacket) session.getDataCache().remove(CacheKey.PACKET_LOGIN_PACKET);
            ClientSettingsPacket ClientSettingsPacket = new ClientSettingsPacket(loginpacket.decoded.language, 8, ChatVisibility.FULL, false, new SkinPart[]{}, Hand.MAIN_HAND);
            ((PCDownstreamSession) session.getDownstream()).send(ClientSettingsPacket);

            UpdateAttributesPacket attr = new UpdateAttributesPacket();
            attr.rtid = entityPlayer.proxyEid;
            if (entityPlayer.attributes.isEmpty())
            {
                attr.entries = new ArrayList();
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.ABSORPTION));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.EXHAUSTION));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.HUNGER));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL));
                attr.entries.add(PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED));
            }
            else
            {
                attr.entries = entityPlayer.attributes.values();
            }
            session.sendPacket(attr);

            AdventureSettingsPacket adv = new AdventureSettingsPacket();
            //flags
            adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, restored.getGameMode().equals(GameMode.ADVENTURE));
//			adv.setFlag(AdventureSettingsPacket.NO_PVP, true);
//			adv.setFlag(AdventureSettingsPacket.AUTO_JUMP, true);
            adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, restored.getGameMode().equals(GameMode.CREATIVE) || restored.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.NO_CLIP, restored.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.WORLD_BUILDER, !restored.getGameMode().equals(GameMode.SPECTATOR) || !restored.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.FLYING, false);
            adv.setFlag(AdventureSettingsPacket.MUTED, false);
            //custom permission flags (not necessary for now when using LEVEL_PERMISSION setting)
//			adv.setFlag(AdventureSettingsPacket.BUILD_AND_MINE, true);
//			adv.setFlag(AdventureSettingsPacket.DOORS_AND_SWITCHES, true);
//			adv.setFlag(AdventureSettingsPacket.OPEN_CONTAINERS, true);
//			adv.setFlag(AdventureSettingsPacket.ATTACK_PLAYERS, true);
//			adv.setFlag(AdventureSettingsPacket.ATTACK_MOBS, true);
//			adv.setFlag(AdventureSettingsPacket.OPERATOR, true);
//			adv.setFlag(AdventureSettingsPacket.TELEPORT, true);
            adv.eid = entityPlayer.proxyEid;
            adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;     //TODO update this with server configiration
            adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER; //TODO update this with server configiration
            session.sendPacket(adv);

            SetEntityDataPacket entityData = new SetEntityDataPacket();
            entityData.rtid = entityPlayer.proxyEid;
            entityData.meta = EntityMetaData.createDefault();
            session.sendPacket(entityData);

            if (session.getProxy().getAuthMode().equalsIgnoreCase("online")) {

                MovePlayerPacket pk = new MovePlayerPacket();
                pk.rtid = entityPlayer.proxyEid;
                pk.mode = MovePlayerPacket.MODE_TELEPORT;
                pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset(), (float) packet.getZ());
                pk.yaw = packet.getYaw();
                pk.pitch = packet.getPitch();
                pk.headYaw = packet.getYaw();

                if (entityPlayer.riding != 0) {
                    CachedEntity vehicle = session.getEntityCache().getByLocalEID(entityPlayer.riding);
                    if (vehicle != null) {
                        pk.ridingRuntimeId = vehicle.eid;
                    }
                }
                session.sendPacket(pk);

                entityPlayer.absoluteMove(packet.getX(), packet.getY() + entityPlayer.peType.getOffset(), packet.getZ(), packet.getYaw(), packet.getPitch());

                /*ChangeDimensionPacket d = new ChangeDimensionPacket();
				d.dimension = 0;
				d.position = new Vector3F((float) packet.getX(), (float) packet.getY() + Constants.PLAYER_HEAD_OFFSET,
						(float) packet.getZ());
				session.sendPacket(d);
				session.sendPacket(new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN));*/
                System.out.println("spawning at " + pk.position.toString());
            }

            session.setSpawned();

            session.getEntityCache().getClientEntity().x = packet.getX();
            session.getEntityCache().getClientEntity().y = packet.getY();
            session.getEntityCache().getClientEntity().z = packet.getZ();

            // send the confirmation
            ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
            ((PCDownstreamSession) session.getDownstream()).send(confirm);

            PlayerListPacket playerListPacket = new PlayerListPacket();
            Set<org.dragonet.proxy.protocol.type.PlayerListEntry> peEntries = new HashSet();

            for (CachedEntity entity : session.getEntityCache().getEntities().values()) {
                if (entity.eid == entityPlayer.eid) { //never send ME (entry 1L)
                    continue;
                }
                if (entity.peType == EntityType.PLAYER) {
                    PlayerListEntry playerListEntry = session.getPlayerInfoCache().get(entity.playerUniqueId);
                    AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
                    pkAddPlayer.eid = entity.proxyEid;
                    pkAddPlayer.rtid = entity.proxyEid;

                    pkAddPlayer.uuid = entity.playerUniqueId;

                    pkAddPlayer.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
                    pkAddPlayer.motion = Vector3F.ZERO;
                    pkAddPlayer.yaw = entity.yaw;
                    pkAddPlayer.pitch = entity.pitch;
                    pkAddPlayer.username = playerListEntry.getProfile().getName();

                    pkAddPlayer.meta = EntityMetaTranslator.translateToPE(session, entity.pcMeta, EntityType.PLAYER);
                    pkAddPlayer.meta.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(playerListEntry.getProfile().getName())); //hacky for now

                    PlayerSkinPacket skin = new PlayerSkinPacket(entity.playerUniqueId);

                    org.dragonet.proxy.protocol.type.PlayerListEntry peEntry = new org.dragonet.proxy.protocol.type.PlayerListEntry();
                    peEntry.uuid = entity.playerUniqueId;
                    peEntry.eid = entity.eid;
                    peEntry.username = playerListEntry.getProfile().getName();
                    peEntry.skin = Skin.DEFAULT_SKIN;
                    peEntry.xboxUserId = "null";
                    peEntries.add(peEntry);

                    session.sendPacket(pkAddPlayer);
                    session.sendPacket(skin);
                } else if (entity.peType == EntityType.ITEM) {
                    AddItemEntityPacket pk = new AddItemEntityPacket();
                    pk.rtid = entity.proxyEid;
                    pk.eid = entity.proxyEid;
                    pk.metadata = EntityMetaTranslator.translateToPE(session, entity.pcMeta, entity.peType);
                    pk.item = ((SlotMeta) pk.metadata.map.get(EntityMetaData.Constants.DATA_TYPE_SLOT)).slot;
                    pk.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
                    pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                    entity.spawned = true;
                    session.sendPacket(pk);
                } else if (entity.peType == EntityType.PAINTING) {
//                    AddPaintingPacket pk = new AddPaintingPacket();
//                    pk.rtid = entity.proxyEid;
//                    pk.eid = entity.proxyEid;
//                    pk.pos = new BlockPosition((int) entity.x, (int) entity.y, (int) entity.z);
//                    pk.direction = 1;
//                    pk.title = "Kebab";
//                    entity.spawned = true;
//                    session.sendPacket(pk);
                } else if (entity.peType != null) { //ENTITY
                    AddEntityPacket pk = new AddEntityPacket();
                    pk.rtid = entity.proxyEid;
                    pk.eid = entity.proxyEid;
                    pk.type = entity.peType.getPeType();
                    pk.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
                    pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                    pk.yaw = entity.yaw;
                    pk.pitch = entity.pitch;
                    pk.meta = EntityMetaTranslator.translateToPE(session, entity.pcMeta, entity.peType);
                    // TODO: Hack for now. ;P
                    pk.attributes = entity.attributes.values();
                    session.sendPacket(pk);
                }
            }

            playerListPacket.type = PlayerListPacket.TYPE_ADD;
            playerListPacket.entries = (org.dragonet.proxy.protocol.type.PlayerListEntry[]) peEntries.toArray(new org.dragonet.proxy.protocol.type.PlayerListEntry[peEntries.size()]);
            session.sendPacket(playerListPacket);
            entityPlayer.spawned = true;
            System.out.println("SPAWNED !");
            return null;
        }

        MovePlayerPacket pk = new MovePlayerPacket();
        pk.rtid = entityPlayer.proxyEid;
        pk.mode = MovePlayerPacket.MODE_TELEPORT;
        pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + EntityType.PLAYER.getOffset(), (float) packet.getZ());
        pk.yaw = packet.getYaw();
        pk.pitch = packet.getPitch();
        pk.headYaw = packet.getYaw();

        if (entityPlayer.riding != 0) {
            CachedEntity vehicle = session.getEntityCache().getByLocalEID(entityPlayer.riding);
            if (vehicle != null) {
                pk.ridingRuntimeId = vehicle.eid;
            }
        }

        entityPlayer.absoluteMove(packet.getX(), packet.getY() + entityPlayer.peType.getOffset(), packet.getZ(), packet.getYaw(), packet.getPitch());

        // send the confirmation
        ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
        ((PCDownstreamSession) session.getDownstream()).send(confirm);

        return new PEPacket[]{pk};
    }
}
