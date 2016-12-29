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
package org.dragonet.proxy.network;

import java.util.HashMap;
import java.util.Map;

import org.dragonet.proxy.network.translator.IgnorePacketTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCBlockChangePacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCChatPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCChunkDataTranslator;
import org.dragonet.proxy.network.translator.pc.PCDestroyEntitiesPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityEffectPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityHeadLookPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityMetadataPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityPositionPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityPositionRotationPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityRemoveEffectPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCEntityVelocityPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCJoinGamePacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCLoginSucessPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCMultiBlockChangePacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCNotifyClientPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCOpenWindowPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCPlaySoundPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCPlayerListItemPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCPlayerPositionRotationPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCSetSlotPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCSpawnPositionPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCUpdateHealthPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCUpdateSignPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCUpdateTimePacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCWindowItemsTranslator;
import org.dragonet.proxy.network.translator.pe.PEChatPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEInteractPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEMovePlayerPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEPlayerActionPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEPlayerEquipmentPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PERequestChunkRadiusPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PERespawnPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEUseItemPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEWindowClosePacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.AnimatePacket;
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

public final class PacketTranslatorRegister {

    private final static Map<Class<? extends Packet>, PCPacketTranslator> PC_TO_PE_TRANSLATOR = new HashMap<>();
    private final static Map<Class<? extends DataPacket>, PEPacketTranslator> PE_TO_PC_TRANSLATOR = new HashMap<>();

    /**
     * PC to PE
     */
    static {
        // Login phase
    	PC_TO_PE_TRANSLATOR.put(LoginSuccessPacket.class, new PCLoginSucessPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerJoinGamePacket.class, new PCJoinGamePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPositionPacket.class, new PCSpawnPositionPacketTranslator());

        //Settings && Weather
        PC_TO_PE_TRANSLATOR.put(ServerNotifyClientPacket.class, new PCNotifyClientPacketTranslator());
        
        // Chat
        PC_TO_PE_TRANSLATOR.put(ServerChatPacket.class, new PCChatPacketTranslator());

        // Map
        PC_TO_PE_TRANSLATOR.put(ServerChunkDataPacket.class, new PCChunkDataTranslator());
        
        PC_TO_PE_TRANSLATOR.put(ServerUpdateTimePacket.class, new PCUpdateTimePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockChangePacket.class, new PCBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerMultiBlockChangePacket.class, new PCMultiBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateTileEntityPacket.class, new PCUpdateSignPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlaySoundPacket.class, new PCPlaySoundPacketTranslator());

        // Entity
        PC_TO_PE_TRANSLATOR.put(ServerPlayerPositionRotationPacket.class, new PCPlayerPositionRotationPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnMobPacket.class, new PCSpawnMobPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerListEntryPacket.class, new PCPlayerListItemPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnPlayerPacket.class, new PCSpawnPlayerPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnObjectPacket.class, new PCSpawnObjectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityMetadataPacket.class, new PCEntityMetadataPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityDestroyPacket.class, new PCDestroyEntitiesPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionRotationPacket.class, new PCEntityPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityHeadLookPacket.class, new PCEntityHeadLookPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionPacket.class, new PCEntityPositionPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityVelocityPacket.class, new PCEntityVelocityPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEffectPacket.class, new PCEntityEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRemoveEffectPacket.class, new PCEntityRemoveEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerHealthPacket.class, new PCUpdateHealthPacketTranslator());

        //Inventory
        PC_TO_PE_TRANSLATOR.put(ServerOpenWindowPacket.class, new PCOpenWindowPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWindowItemsPacket.class, new PCWindowItemsTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetSlotPacket.class, new PCSetSlotPacketTranslator());
        
    }

/*    org.spacehq.mc.protocol.packet.ingame.server.ServerBossBarPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket.class -> registerDecoder(ProtocolInfo.TEXT_PACKET, TextPacket.class);//
    org.spacehq.mc.protocol.packet.ingame.server.ServerCombatPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerDifficultyPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerPluginMessagePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerSetCompressionPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerSetCooldownPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerStatisticsPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket.class
    
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket.class
    
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket.class
    
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket.class -> registerDecoder(ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET, SpawnExperienceOrbPacket.class);         
    
        																							   { registerDecoder(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket.class } -> { registerDecoder(ProtocolInfo.ADD_HANGING_ENTITY_PACKET, AddHangingEntityPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket.class          }    { registerDecoder(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket.class       }    { registerDecoder(ProtocolInfo.ADD_ITEM_PACKET, AddItemPacket.class);
    
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket.class -> registerDecoder(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket.class -> registerDecoder(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket.class);
    
    org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket.class   }
    org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket.class } -> No Scoreboard Support in PE as of 1.0.0
    org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket.class                }  
    org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket.class         }
    
        org.spacehq.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket.class -> Internal Cache System
    org.spacehq.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket.class -> registerDecoder(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket.class -> registerDecoder(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket.class -> registerDecoder(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket.class -> registerDecoder(ProtocolInfo.CONTAINER_SET_CONTENT_PACKET, ContainerSetContentPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket.class -> registerDecoder(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket.class);

    org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerExplosionPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerMapDataPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket.class -> registerDecoder(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket.class -> registerDecoder(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket.class
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket.class -> registerDecoder(ProtocolInfo.SET_TIME_PACKET, SetTimePacket.class);
    org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket.class*/
    
    /*
        registerDecoder(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, PEClientConnectPacket.class); // Client Connect 0x09

        registerDecoder(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        registerDecoder(ProtocolInfo.ANIMATE_PACKET, AnimatePacket.class);//
        registerDecoder(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, AvailableCommandsPacket.class);
        registerDecoder(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
        registerDecoder(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);//
        registerDecoder(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket.class);
        registerDecoder(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket.class);
        registerDecoder(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket.class);
        registerDecoder(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket.class);
        registerDecoder(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket.class);
        registerDecoder(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);//
        registerDecoder(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);//
        registerDecoder(ProtocolInfo.EXPLODE_PACKET, ExplodePacket.class);
        registerDecoder(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);//
        registerDecoder(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);//
        registerDecoder(ProtocolInfo.INVENTORY_ACTION_PACKET, InventoryActionPacket.class);//
        registerDecoder(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket.class);//
        registerDecoder(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket.class);
        registerDecoder(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);//
        registerDecoder(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);//
        registerDecoder(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        registerDecoder(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);//
        registerDecoder(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket.class);
        registerDecoder(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket.class);
        registerDecoder(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);//
        registerDecoder(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        registerDecoder(ProtocolInfo.REPLACE_SELECTED_ITEM_PACKET, ReplaceSelectedItemPacket.class);
        registerDecoder(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket.class);
        registerDecoder(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);//
        registerDecoder(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
        registerDecoder(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);//
        registerDecoder(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket.class);
        registerDecoder(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket.class);
        registerDecoder(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);
        registerDecoder(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket.class);
        registerDecoder(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        registerDecoder(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        registerDecoder(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        registerDecoder(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);
        registerDecoder(ProtocolInfo.START_GAME_PACKET, StartGamePacket.class);
        registerDecoder(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket.class);
        registerDecoder(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerDecoder(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket.class);//
        registerDecoder(ProtocolInfo.BATCH_PACKET, BatchPacket.class);//
     */
    
    
    
    
    
    

    /*
        																						{ org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket.class
        PE_TO_PC_TRANSLATOR.put(MovePlayerPacket.class, new PEMovePlayerPacketTranslator()); -> { org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket.class
    																							{ org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket.class
        
        
        PE_TO_PC_TRANSLATOR.put(PlayerActionPacket.class, new PEPlayerActionPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(InteractPacket.class, new PEInteractPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobEquipmentPacket.class, new PEPlayerEquipmentPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RequestChunkRadiusPacket.class, new PERequestChunkRadiusPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AnimatePacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(BlockEntityDataPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CommandStepPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerSetSlotPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CraftingEventPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(DropItemPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(EntityEventPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(FullChunkDataPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(HurtArmorPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(InventoryActionPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ItemFrameDropItemPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobArmorEquipmentPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RemoveBlockPacket.class, new IgnorePacketTranslator());
        
        PE_TO_PC_TRANSLATOR.put(RespawnPacket.class, new PERespawnPacketTranslator()); ->     org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket.class
     */
    
/*    PE_TO_PC_TRANSLATOR.put(TextPacket.class, new PEChatPacketTranslator()); -> org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket.class
 * 
    org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket.class
    org.spacehq.mc.protocol.packet.ingame.client.ClientPluginMessagePacket.class -> No Plugin Message support in PE as of 1.0.0

    org.spacehq.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket.class
    org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket.class
    PE_TO_PC_TRANSLATOR.put(UseItemPacket.class, new PEUseItemPacketTranslator()); -> org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket.class
    PE_TO_PC_TRANSLATOR.put(ContainerClosePacket.class, new PEWindowClosePacketTranslator()); -> org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket.class -> Internal Cache System
    org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.window.ClientEnchantItemPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.world.ClientSpectatePacket.class
    PE_TO_PC_TRANSLATOR.put(PlayerInputPacket.class, new IgnorePacketTranslator()); -> org.spacehq.mc.protocol.packet.ingame.client.world.ClientSteerBoatPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket.class
    org.spacehq.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket.class
    org.spacehq.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket.class
    */
    
    /*
     PlayerState
     */
    
    
    /**
     * PE to PC
     */
    static {
        // Chat
        PE_TO_PC_TRANSLATOR.put(TextPacket.class, new PEChatPacketTranslator());

        // Entity
        PE_TO_PC_TRANSLATOR.put(UseItemPacket.class, new PEUseItemPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MovePlayerPacket.class, new PEMovePlayerPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerActionPacket.class, new PEPlayerActionPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(InteractPacket.class, new PEInteractPacketTranslator());
        
        //Inventory
        PE_TO_PC_TRANSLATOR.put(ContainerClosePacket.class, new PEWindowClosePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobEquipmentPacket.class, new PEPlayerEquipmentPacketTranslator());
        
        PE_TO_PC_TRANSLATOR.put(RequestChunkRadiusPacket.class, new PERequestChunkRadiusPacketTranslator());
        
        // Unfinished
        PE_TO_PC_TRANSLATOR.put(AnimatePacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(BlockEntityDataPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CommandStepPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerSetSlotPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CraftingEventPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(DropItemPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(EntityEventPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(FullChunkDataPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(HurtArmorPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(InventoryActionPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ItemFrameDropItemPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobArmorEquipmentPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerInputPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RemoveBlockPacket.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RespawnPacket.class, new PERespawnPacketTranslator());
    }

    public static DataPacket[] translateToPE(UpstreamSession session, Packet packet) {
        if (packet == null) {
            return null;
        }
        
        PCPacketTranslator<Packet> target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if (target == null) {
        	System.err.println("[PC to PE] No translator found for : " + packet.getClass().getName());
            return null;
        }
        try {
        	System.out.println(">>> " + packet.getClass().getName());
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static Packet[] translateToPC(UpstreamSession session, DataPacket packet) {
        if (packet == null) {
            return null;
        }
        
        PEPacketTranslator<DataPacket> target = PE_TO_PC_TRANSLATOR.get(packet.getClass());
        if (target == null) {
        	System.err.println("[PE to PC] No translator found for : " + packet.getClass().getName());
            return null;
        }
        try {
        	System.out.println("<<< " + packet.getClass().getName());
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
