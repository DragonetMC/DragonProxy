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
package org.dragonet.proxy.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.CommandStepPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.CraftingEventPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DropItemPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.FullChunkDataPacket;
import cn.nukkit.network.protocol.HurtArmorPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.InventoryActionPacket;
import cn.nukkit.network.protocol.ItemFrameDropItemPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RemoveBlockPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UseItemPacket;

public final class Protocol {

    private final static HashMap<Byte, Class<? extends DataPacket>> protocol;

    static {
        protocol = new HashMap<>();
        registerDecoder(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, PEClientConnectPacket.class); // Client Connect 0x09
        registerDecoder(ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET, PEClientHandshakePacket.class);
        //registerDecoder(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket.class);
        //registerDecoder(ProtocolInfo.ADD_HANGING_ENTITY_PACKET, AddHangingEntityPacket.class);
        //registerDecoder(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        //registerDecoder(ProtocolInfo.ADD_ITEM_PACKET, AddItemPacket.class);
        //registerDecoder(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket.class);
        //registerDecoder(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket.class);
        //registerDecoder(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        registerDecoder(ProtocolInfo.ANIMATE_PACKET, AnimatePacket.class);
        //registerDecoder(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, AvailableCommandsPacket.class);
        //registerDecoder(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
        registerDecoder(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        //registerDecoder(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket.class);
        //registerDecoder(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket.class);
        //registerDecoder(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket.class);
        //registerDecoder(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket.class);
        registerDecoder(ProtocolInfo.COMMAND_STEP_PACKET, CommandStepPacket.class);
        registerDecoder(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        //registerDecoder(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket.class);
        //registerDecoder(ProtocolInfo.CONTAINER_SET_CONTENT_PACKET, ContainerSetContentPacket.class);
        //registerDecoder(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket.class);
        registerDecoder(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket.class);
        //registerDecoder(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket.class);
        registerDecoder(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);
        //registerDecoder(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(ProtocolInfo.DROP_ITEM_PACKET, DropItemPacket.class);
        registerDecoder(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);
        //registerDecoder(ProtocolInfo.EXPLODE_PACKET, ExplodePacket.class);
        registerDecoder(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        registerDecoder(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket.class);
        registerDecoder(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);
        registerDecoder(ProtocolInfo.INVENTORY_ACTION_PACKET, InventoryActionPacket.class);
        registerDecoder(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket.class);
        //registerDecoder(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket.class);
        //registerDecoder(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket.class);
        registerDecoder(ProtocolInfo.LOGIN_PACKET, LoginPacket.class);
        registerDecoder(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);
        registerDecoder(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        //registerDecoder(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        registerDecoder(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerDecoder(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        registerDecoder(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket.class);
        //registerDecoder(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket.class);
        //registerDecoder(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket.class);
        registerDecoder(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        //registerDecoder(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        //registerDecoder(ProtocolInfo.REPLACE_SELECTED_ITEM_PACKET, ReplaceSelectedItemPacket.class);
        //registerDecoder(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket.class);
        registerDecoder(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);
        //registerDecoder(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class); // Conflicts with Client Connect Packet
        //registerDecoder(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
        registerDecoder(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);
        //registerDecoder(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket.class);
        //registerDecoder(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket.class);
        //registerDecoder(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);
        //registerDecoder(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket.class);
        //registerDecoder(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        //registerDecoder(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        //registerDecoder(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        //registerDecoder(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);
        //registerDecoder(ProtocolInfo.SET_TIME_PACKET, SetTimePacket.class);
        //registerDecoder(ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET, SpawnExperienceOrbPacket.class);
        //registerDecoder(ProtocolInfo.START_GAME_PACKET, StartGamePacket.class);
        //registerDecoder(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket.class);
        registerDecoder(ProtocolInfo.TEXT_PACKET, TextPacket.class);
        //registerDecoder(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerDecoder(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket.class);
        registerDecoder(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
    }

    private static void registerDecoder(byte id, Class<? extends DataPacket> clazz) {
        if (protocol.containsKey(id)) {
            return;
        }
        try {
            //clazz.getConstructor(byte[].class);
        	clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
        	//TODO: Remove
        	ex.printStackTrace();
            return;
        }
        protocol.put(id, clazz);
    }

    public static DataPacket decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        byte pid = data[0];
        if (protocol.containsKey(pid)) {
            Class<? extends DataPacket> c = protocol.get(pid);
            try {
            	//DataPacket pk = c.getDeclaredConstructor(byte[].class).newInstance((Object) data);
            	DataPacket pk = c.getDeclaredConstructor().newInstance();
            	pk.setBuffer(data, 1);
                pk.decode();
                return pk;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            	ex.printStackTrace();
            } catch (Exception e){
            	e.printStackTrace();
            }
        }
        System.err.println("Failed to decode packet with ID " + pid + "\n\tData: " + arrayToString(data));
        //return new UnknownPacket(pid, data);
        return null;
    }

	private static String arrayToString(byte[] data) {
		StringBuilder str = new StringBuilder();
		for(byte b : data){
			str.append(b + " ");
		}
		return str.toString();
	}
}