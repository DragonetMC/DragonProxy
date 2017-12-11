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
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.entity.PEEntityAttribute;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.PCDownstreamSession;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.HashSet;
import java.util.Set;
import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.entity.meta.type.ByteArrayMeta;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.protocol.packets.*;
import org.dragonet.proxy.protocol.type.Skin;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.Constants;
import org.dragonet.proxy.utilities.Vector3F;

public class PCPlayerPositionRotationPacketTranslator
		implements IPCPacketTranslator<ServerPlayerPositionRotationPacket> {
	// vars

	// constructor
	public PCPlayerPositionRotationPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
		if (!session.isSpawned()) {
			System.out.println("SPAWNED! ");
			if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
				session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
				return null;
			}

			ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache()
					.remove(CacheKey.PACKET_JOIN_GAME_PACKET);
			if (!session.getProxy().getAuthMode().equalsIgnoreCase("online")) {
				StartGamePacket ret = new StartGamePacket();
				ret.rtid = 1L;
				ret.eid = 1L; // Use EID 0 for easier management
				ret.dimension = 0;
				ret.seed = 0;
				ret.generator = 1;
				ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
				ret.spawnPosition = new BlockPosition((int) packet.getX(), (int) packet.getY(), (int) packet.getZ());
				ret.position = new Vector3F((float) packet.getX(), (float) packet.getY() + Constants.PLAYER_HEAD_OFFSET,
						(float) packet.getZ());
				ret.levelId = "";
				ret.worldName = "World";
				ret.commandsEnabled = true;
				ret.defaultPlayerPermission = 2;
				ret.premiumWorldTemplateId = "";
				session.sendPacket(ret);
			}
                        
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("DragonProxy");
                        ClientPluginMessagePacket ClientPluginMessagePacket = new ClientPluginMessagePacket("MC|Brand" , out.toByteArray());
                        ((PCDownstreamSession) session.getDownstream()).send(ClientPluginMessagePacket);

                        LoginPacket loginpacket = (LoginPacket) session.getDataCache().remove(CacheKey.PACKET_LOGIN_PACKET);
                        ClientSettingsPacket ClientSettingsPacket = new ClientSettingsPacket(loginpacket.decoded.language, 8, ChatVisibility.FULL, false, new SkinPart[]{}, Hand.MAIN_HAND);
                        ((PCDownstreamSession) session.getDownstream()).send(ClientSettingsPacket);

			UpdateAttributesPacket attr = new UpdateAttributesPacket();
			attr.rtid = 1L;
			attr.entries = new PEEntityAttribute[] { PEEntityAttribute.findAttribute(PEEntityAttribute.ABSORPTION),
					PEEntityAttribute.findAttribute(PEEntityAttribute.EXHAUSTION),
					PEEntityAttribute.findAttribute(PEEntityAttribute.HUNGER),
					PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL),
					PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE),
					PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL),
					PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED), };
			session.sendPacket(attr);

			AdventureSettingsPacket adv = new AdventureSettingsPacket();
			adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, restored.getGameMode().equals(GameMode.SPECTATOR));
			// adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, true);
			adv.setFlag(AdventureSettingsPacket.ATTACK_PLAYERS, true);
			adv.setFlag(AdventureSettingsPacket.ATTACK_MOBS, true);
			adv.setFlag(AdventureSettingsPacket.BUILD_AND_MINE, true);
			adv.setFlag(AdventureSettingsPacket.OPERATOR, true);
			adv.setFlag(AdventureSettingsPacket.TELEPORT, true);
			adv.setFlag(AdventureSettingsPacket.NO_CLIP, restored.getGameMode().equals(GameMode.SPECTATOR));
			adv.setFlag(AdventureSettingsPacket.FLYING, false);
			adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;     //TODO update this with server configiration
			adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER; //TODO update this with server configiration
			session.sendPacket(adv);

			SetEntityDataPacket entityData = new SetEntityDataPacket();
			entityData.rtid = 1L;
			entityData.meta = EntityMetaData.createDefault();
			session.sendPacket(entityData);

			if (session.getProxy().getAuthMode().equalsIgnoreCase("online")) {
				MovePlayerPacket pk = new MovePlayerPacket();
				pk.rtid = 1L;
				pk.mode = MovePlayerPacket.MODE_TELEPORT;
				pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + Constants.PLAYER_HEAD_OFFSET,
						(float) packet.getZ());
				pk.yaw = packet.getYaw();
				pk.pitch = packet.getPitch();
				pk.headYaw = packet.getYaw();
				session.sendPacket(pk);

				CachedEntity cliEntity = session.getEntityCache().getClientEntity();
				cliEntity.x = packet.getX();
				cliEntity.y = packet.getY() + Constants.PLAYER_HEAD_OFFSET;
				cliEntity.z = packet.getZ();
				cliEntity.yaw = packet.getYaw();
				cliEntity.pitch = packet.getPitch();

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
			session.getEntityCache().getClientEntity().y = packet.getY() + Constants.PLAYER_HEAD_OFFSET;
			session.getEntityCache().getClientEntity().z = packet.getZ();

			// send the confirmation
			ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
			((PCDownstreamSession) session.getDownstream()).send(confirm);
                        
                        PlayerListPacket playerListPacket = new PlayerListPacket();
                        Set<org.dragonet.proxy.protocol.type.PlayerListEntry> peEntries = new HashSet();
                        
                        for(CachedEntity entity : session.getEntityCache().getEntities().values())
                        {
                            if (entity.eid == 1L) //never send ME (entry 1L)
                                continue;
                            if (entity.player && entity.playerUniqueId != null) //PLAYER
                            {
                                PlayerListEntry playerListEntry = session.getPlayerInfoCache().get(entity.playerUniqueId);
                                AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
                                pkAddPlayer.eid = entity.proxyEid;
                                pkAddPlayer.rtid = entity.proxyEid;

                                pkAddPlayer.uuid = entity.playerUniqueId;

                                pkAddPlayer.position = new Vector3F((float) entity.x, (float) entity.y + Constants.PLAYER_HEAD_OFFSET, (float) entity.z);
                                pkAddPlayer.motion = Vector3F.ZERO;
                                pkAddPlayer.yaw = entity.yaw;
                                pkAddPlayer.pitch = entity.pitch;
                                pkAddPlayer.username = playerListEntry.getProfile().getName();
                                
                                pkAddPlayer.meta = EntityMetaTranslator.translateToPE(entity.pcMeta, EntityType.PLAYER);
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
                            }
                            else if (entity.peType != null) //ENTITY
                            {
                                AddEntityPacket pk = new AddEntityPacket();
                                pk.rtid = entity.proxyEid;
                                pk.eid = entity.proxyEid;
                                pk.type = entity.peType.getPeType();
                                pk.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
                                pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                                pk.meta = EntityMetaTranslator.translateToPE(entity.pcMeta, entity.peType);
                                // TODO: Hack for now. ;P
                                pk.attributes = new PEEntityAttribute[]{};
                                session.sendPacket(pk);
                            }
                            else // ITEM
                            {
                                AddItemEntityPacket pk = new AddItemEntityPacket();
                                pk.rtid = entity.proxyEid;
                                pk.eid = entity.proxyEid;
                                pk.position = new Vector3F((float) entity.x, (float) entity.y, (float) entity.z);
                                pk.motion = new Vector3F((float) entity.motionX, (float) entity.motionY, (float) entity.motionZ);
                                pk.item = ItemBlockTranslator.translateSlotToPE(new ItemStack(1, 1));
                                pk.metadata = EntityMetaTranslator.translateToPE(entity.pcMeta, EntityType.ITEM);
                                session.sendPacket(pk);//not working for now
                            }
                        }
                        
                        playerListPacket.type = PlayerListPacket.TYPE_ADD;
                        playerListPacket.entries = (org.dragonet.proxy.protocol.type.PlayerListEntry[])peEntries.toArray(new org.dragonet.proxy.protocol.type.PlayerListEntry[peEntries.size()]);
                        session.sendPacket(playerListPacket);
                    return null;
                }

		MovePlayerPacket pk = new MovePlayerPacket();
		pk.rtid = 1L;
		pk.mode = MovePlayerPacket.MODE_TELEPORT;
		pk.position = new Vector3F((float) packet.getX(), (float) packet.getY() + Constants.PLAYER_HEAD_OFFSET,
				(float) packet.getZ());
		pk.yaw = packet.getYaw();
		pk.pitch = packet.getPitch();
		pk.headYaw = packet.getYaw();
		CachedEntity cliEntity = session.getEntityCache().getClientEntity();
		cliEntity.x = packet.getX();
		cliEntity.y = packet.getY() + Constants.PLAYER_HEAD_OFFSET;
		cliEntity.z = packet.getZ();
		cliEntity.yaw = packet.getYaw();
		cliEntity.pitch = packet.getPitch();

		// session.sendChat(String.format("FORCING TO (%.2f, %.2f, %.2f", packet.getX(),
		// packet.getY(), packet.getZ()));

		// send the confirmation
		ClientTeleportConfirmPacket confirm = new ClientTeleportConfirmPacket(packet.getTeleportId());
		((PCDownstreamSession) session.getDownstream()).send(confirm);

		return new PEPacket[] { pk };
	}

	// private

}
