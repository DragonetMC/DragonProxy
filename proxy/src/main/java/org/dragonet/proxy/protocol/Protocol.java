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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddHangingEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.AddItemPacket;
import cn.nukkit.network.protocol.AddPaintingPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.CommandStepPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.CraftingEventPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DisconnectPacket;
import cn.nukkit.network.protocol.DropItemPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.ExplodePacket;
import cn.nukkit.network.protocol.FullChunkDataPacket;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.network.protocol.HurtArmorPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.InventoryActionPacket;
import cn.nukkit.network.protocol.ItemFrameDropItemPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RemoveBlockPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.ReplaceSelectedItemPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.ResourcePackClientResponsePacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.SetCommandsEnabledPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.SetHealthPacket;
import cn.nukkit.network.protocol.SetPlayerGameTypePacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.SpawnExperienceOrbPacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.network.protocol.UseItemPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;

public final class Protocol {
	
	private final static Class<? extends DataPacket>[] packetPool = new Class[256];
	static {
		registerPackets();
	}
	
    private static void registerPacket(byte id, Class<? extends DataPacket> clazz) {
        packetPool[id & 0xff] = clazz;
    }
	
    private static void registerPackets() {
        registerPacket(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket.class);
        registerPacket(ProtocolInfo.ADD_HANGING_ENTITY_PACKET, AddHangingEntityPacket.class);
        registerPacket(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        registerPacket(ProtocolInfo.ADD_ITEM_PACKET, AddItemPacket.class);
        registerPacket(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket.class);
        registerPacket(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket.class);
        registerPacket(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        registerPacket(ProtocolInfo.ANIMATE_PACKET, AnimatePacket.class);
        registerPacket(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, AvailableCommandsPacket.class);
        registerPacket(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
        registerPacket(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        registerPacket(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket.class);
        registerPacket(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket.class);
        registerPacket(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket.class);
        registerPacket(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket.class);
        registerPacket(ProtocolInfo.COMMAND_STEP_PACKET, CommandStepPacket.class);
        registerPacket(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        registerPacket(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket.class);
        registerPacket(ProtocolInfo.CONTAINER_SET_CONTENT_PACKET, ContainerSetContentPacket.class);
        registerPacket(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket.class);
        registerPacket(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket.class);
        registerPacket(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket.class);
        registerPacket(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);
        registerPacket(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket.class);
        registerPacket(ProtocolInfo.DROP_ITEM_PACKET, DropItemPacket.class);
        registerPacket(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);
        registerPacket(ProtocolInfo.EXPLODE_PACKET, ExplodePacket.class);
        registerPacket(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        registerPacket(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket.class);
        registerPacket(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);
        registerPacket(ProtocolInfo.INVENTORY_ACTION_PACKET, InventoryActionPacket.class);
        registerPacket(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket.class);
        registerPacket(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket.class);
        registerPacket(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket.class);
        registerPacket(ProtocolInfo.LOGIN_PACKET, LoginPacket.class);
        registerPacket(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);
        registerPacket(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        registerPacket(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        registerPacket(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerPacket(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        registerPacket(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket.class);
        registerPacket(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket.class);
        registerPacket(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket.class);
        registerPacket(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        registerPacket(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        registerPacket(ProtocolInfo.REPLACE_SELECTED_ITEM_PACKET, ReplaceSelectedItemPacket.class);
        registerPacket(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket.class);
        registerPacket(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);
        registerPacket(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class);
        registerPacket(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
        registerPacket(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);
        registerPacket(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket.class);
        registerPacket(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket.class);
        registerPacket(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);
        registerPacket(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket.class);
        registerPacket(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        registerPacket(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        registerPacket(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        registerPacket(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);
        registerPacket(ProtocolInfo.SET_TIME_PACKET, SetTimePacket.class);
        registerPacket(ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET, SpawnExperienceOrbPacket.class);
        registerPacket(ProtocolInfo.START_GAME_PACKET, StartGamePacket.class);
        registerPacket(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket.class);
        registerPacket(ProtocolInfo.TEXT_PACKET, TextPacket.class);
        registerPacket(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerPacket(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket.class);
        registerPacket(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
    }
	
    //private final static HashMap<Byte, Class<? extends DataPacket>> protocol;
    /*static {
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
    }*/
	
/*    private static void registerDecoder(byte id, Class<? extends DataPacket> clazz) {
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
    }*/

	private static String arrayToString(byte[] data) {
		StringBuilder str = new StringBuilder();
		for(byte b : data){
			str.append(b + " ");
		}
		return str.toString();
	}
    
    public static DataPacket[] decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        
        try {
	        DataPacket pk = getPacket(data[0]);
	        if (pk != null) {
		        if(pk instanceof BatchPacket){
		        	//pk.setBuffer(data);
		        	pk.setBuffer(data, 1);
		        	pk.decode();
		        	return processBatch((BatchPacket) pk);
		    	}
	
            	//DataPacket pk = c.getDeclaredConstructor(byte[].class).newInstance((Object) data);
            	//DataPacket pk = c.getDeclaredConstructor().newInstance();
            	pk.setBuffer(data, 1);
                pk.decode();
                return new DataPacket[] {pk};
	        }
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        System.err.println("Failed to decode packet with ID " + data[0] + "\n\tData: " + arrayToString(data));
        //return new UnknownPacket(pid, data);
        return new DataPacket[0];
    }
	
    private static DataPacket getPacket(byte id) {
        Class<? extends DataPacket> clazz = packetPool[id & 0xff];
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                Server.getInstance().getLogger().logException(e);
            }
        }
        System.err.println("Unknown packet ID: " + id);
        return null;
    }
    
    private static DataPacket[] processBatch(BatchPacket packet) {
        byte[] data;
        try {
            data = Zlib.inflate(packet.payload, 64 * 1024 * 1024);
        } catch (Exception e) {
        	e.printStackTrace();
            return new DataPacket[0];
        }

        //System.out.println(arrayToString(data));
        int len = data.length;
        BinaryStream stream = new BinaryStream(data);
        try {
            List<DataPacket> packets = new ArrayList<>();
            while (stream.offset < len) {
                byte[] buf = stream.getByteArray();

                DataPacket pk;
                if ((pk = getPacket(buf[0])) != null) {
                    if (pk.pid() == ProtocolInfo.BATCH_PACKET) {
                        throw new IllegalStateException("Invalid BatchPacket inside BatchPacket");
                    }
                    pk.setBuffer(buf, 1);
                    pk.decode();
                    packets.add(pk);
                }
            }
            return packets.toArray(new DataPacket[0]);

        } catch (Exception e) {
            System.err.println("BatchPacket 0x" + Binary.bytesToHexString(packet.payload));
            e.printStackTrace();
        }
        return new DataPacket[0];
    }

}