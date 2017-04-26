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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.marfgamer.jraknet.RakNetPacket;

import org.dragonet.proxy.DragonProxy;
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
import org.dragonet.proxy.network.translator.pc.PCSpawnMobPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCSpawnObjectPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCSpawnPlayerPacketTranslator;
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
import org.spacehq.mc.protocol.packet.ingame.client.*;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.client.window.*;
import org.spacehq.mc.protocol.packet.ingame.client.world.*;
import org.spacehq.mc.protocol.packet.ingame.server.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.*;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.*;
import org.spacehq.mc.protocol.packet.ingame.server.window.*;
import org.spacehq.mc.protocol.packet.ingame.server.world.*;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.packetlib.packet.Packet;
import sul.protocol.pocket107.play.*;
import sul.protocol.pocket107.play.Text.Chat;

public final class PacketTranslatorRegister {

    private final static Map<Class<? extends Packet>, PCPacketTranslator> PC_TO_PE_TRANSLATOR = new HashMap<>();
    private final static Map<Class<? extends sul.utils.Packet>, PEPacketTranslator> PE_TO_PC_TRANSLATOR = new HashMap<>();

    /**
     * PC to PE
     */
    static {
        /*PC_TO_PE_TRANSLATOR.put(ClientChatPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientCloseWindowPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientConfirmTransactionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientCreativeInventoryActionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientEnchantItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientKeepAlivePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerAbilitiesPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerActionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerChangeHeldItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerInteractEntityPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerMovementPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerPlaceBlockPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerPositionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerPositionRotationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerRotationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerStatePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerSwingArmPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPlayerUseItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientPluginMessagePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientRequestPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientResourcePackStatusPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientSettingsPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientSpectatePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientSteerBoatPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientSteerVehiclePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientTabCompletePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientTeleportConfirmPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientUpdateSignPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientVehicleMovePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ClientWindowActionPacket.class, new IgnorePacketTranslator());*/
        
        /*PC_TO_PE_TRANSLATOR.put(ServerBlockBreakAnimPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockChangePacket.class, new PCBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockValuePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBossBarPacket.class, new IgnorePacketTranslator());*/
        PC_TO_PE_TRANSLATOR.put(ServerChatPacket.class, new PCChatPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerChunkDataPacket.class, new PCChunkDataTranslator());
        /*PC_TO_PE_TRANSLATOR.put(ServerCloseWindowPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerCombatPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerConfirmTransactionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDifficultyPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDisconnectPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDisplayScoreboardPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityAnimationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityAttachPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityCollectItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityDestroyPacket.class, new PCDestroyEntitiesPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEffectPacket.class, new PCEntityEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEquipmentPacket.class, new IgnorePacketTranslator());*/
        PC_TO_PE_TRANSLATOR.put(ServerEntityHeadLookPacket.class, new PCEntityHeadLookPacketTranslator());
        /*PC_TO_PE_TRANSLATOR.put(ServerEntityMetadataPacket.class, new PCEntityMetadataPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityMovementPacket.class, new IgnorePacketTranslator());*/
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionPacket.class, new PCEntityPositionPacketTranslator());
        /*PC_TO_PE_TRANSLATOR.put(ServerEntityPositionRotationPacket.class, new PCEntityPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPropertiesPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRemoveEffectPacket.class, new PCEntityRemoveEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRotationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntitySetPassengersPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityStatusPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityTeleportPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityVelocityPacket.class, new PCEntityVelocityPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerExplosionPacket.class, new IgnorePacketTranslator());*/
        PC_TO_PE_TRANSLATOR.put(ServerJoinGamePacket.class, new PCJoinGamePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerKeepAlivePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(LoginSuccessPacket.class, new PCLoginSucessPacketTranslator()); //Don't know why this wasn't here, adding
        /*PC_TO_PE_TRANSLATOR.put(ServerMapDataPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerMultiBlockChangePacket.class, new PCMultiBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerNotifyClientPacket.class, new PCNotifyClientPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerOpenTileEntityEditorPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerOpenWindowPacket.class, new PCOpenWindowPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayBuiltinSoundPacket.class, new PCPlaySoundPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayEffectPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerAbilitiesPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerChangeHeldItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerHealthPacket.class, new PCUpdateHealthPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerListDataPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerListEntryPacket.class, new PCPlayerListItemPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerPositionRotationPacket.class, new PCPlayerPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerSetExperiencePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerUseBedPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlaySoundPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPluginMessagePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerResourcePackSendPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerRespawnPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerScoreboardObjectivePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetCompressionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetCooldownPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetSlotPacket.class, new PCSetSlotPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnExpOrbPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnGlobalEntityPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnMobPacket.class, new PCSpawnMobPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnObjectPacket.class, new PCSpawnObjectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPaintingPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnParticlePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPlayerPacket.class, new PCSpawnPlayerPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPositionPacket.class, new PCSpawnPositionPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerStatisticsPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSwitchCameraPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTabCompletePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTeamPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTitlePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUnloadChunkPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateScorePacket.class, new IgnorePacketTranslator());*/
        //PC_TO_PE_TRANSLATOR.put(ServerUpdateTileEntityPacket.class, new PCUpdateSignPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerUpdateTimePacket.class, new PCUpdateTimePacketTranslator()); // This is causing array index exceptions
        /*PC_TO_PE_TRANSLATOR.put(ServerVehicleMovePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWindowItemsPacket.class, new PCWindowItemsTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWindowPropertyPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWorldBorderPacket.class, new IgnorePacketTranslator());*/
    }

    /**
     * PE to PC
     */
    static {
        PE_TO_PC_TRANSLATOR.put(AddEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AddHangingEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AddItemEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AddItem.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AddPainting.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AddPlayer.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AdventureSettings.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Animate.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(AvailableCommands.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Batch.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(BlockEntityData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(BlockEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(BossEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Camera.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ChangeDimension.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ChunkRadiusUpdated.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ClientboundMapItemData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ClientMagic.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CommandStep.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerClose.class, new PEWindowClosePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerOpen.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerSetContent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerSetData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ContainerSetSlot.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CraftingData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(CraftingEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Disconnect.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(DropItem.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(EntityEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Explode.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(FullChunkData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(HurtArmor.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Interact.class, new PEInteractPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(InventoryAction.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ItemFrameDropItem.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(LevelEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(LevelSoundEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Login.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MapInfoRequest.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobArmorEquipment.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobEffect.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MobEquipment.class, new PEPlayerEquipmentPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MoveEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(MovePlayer.class, new PEMovePlayerPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerAction.class, new PEPlayerActionPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerFall.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerInput.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayerList.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(PlayStatus.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RemoveBlock.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RemoveEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ReplaceSelectedItem.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RequestChunkRadius.class, new PERequestChunkRadiusPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ResourcePackClientResponse.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ResourcePacksInfo.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Respawn.class, new PERespawnPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(RiderJump.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ServerHandshake.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetCheatsEnabled.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetDifficulty.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetEntityData.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetEntityLink.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetEntityMotion.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetHealth.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetPlayerGametype.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetSpawnPosition.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SetTime.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(ShowCredits.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(SpawnExperienceOrb.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(StartGame.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(TakeItemEntity.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(TelemetryEvent.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(Chat.class, new PEChatPacketTranslator());
        PE_TO_PC_TRANSLATOR.put(UpdateAttributes.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(UpdateBlock.class, new IgnorePacketTranslator());
        PE_TO_PC_TRANSLATOR.put(UseItem.class, new PEUseItemPacketTranslator());
    }

    public static RakNetPacket[] translateToPE(ClientConnection session, Packet packet) {
        if (packet == null) {
            return new RakNetPacket[0];
        }

        PCPacketTranslator<Packet> target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            DragonProxy.getLogger().warning("[PC to PE] No translator found for : " + packet.getClass().getName());
            return new RakNetPacket[0];
        }
        try {
            DragonProxy.getLogger().info("[PC to PE] >>> " + packet.getClass().getName());
            sul.utils.Packet[] packets = target.translate(session, packet);
            RakNetPacket[] rakPack = new RakNetPacket[packets.length];
            for(int index = 0; index < packets.length; index++){
                rakPack[index] = RakNetUtil.prepareToSend(packets[index]);
            }
            return rakPack;
        } catch (Exception e) {
            e.printStackTrace();
            return new RakNetPacket[0];
        }
    }

    public static Packet[] translateToPC(ClientConnection session, sul.utils.Packet packet) {
        if (packet == null) {
            return new Packet[0];
        }
        
        PEPacketTranslator<sul.utils.Packet> target = PE_TO_PC_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            DragonProxy.getLogger().warning("[PE to PC] No translator found for : " + packet);
            return new Packet[0];
        }

        try {
            DragonProxy.getLogger().info("[PE to PC] <<< " + packet.getClass().getName());
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return new Packet[0];
        }
    }

    /*Deprecated
    public static RakNetPacket[] preparePacketsForSending(sul.utils.Packet... packets){
        RakNetPacket[] pk = new RakNetPacket[packets.length];
        for(int index = 0; index < packets.length; index++){
            pk[index] = preparePacketForSending(packets[index]);
        }
        return pk;
    }*/
    
    /*@Deprecated
    public static RakNetPacket preparePacketForSending(sul.utils.Packet packet) {
        DragonProxy.getLogger().debug("[Translator] Preping PE packet: " + packet.getClass().getCanonicalName());
        
        byte[] buff = packet.encode();
        
        if(buff.length > 512){
            DragonProxy.getLogger().debug("[Translator] Batching packet: " + packet.getClass().getCanonicalName());
            Batch batch = new Batch(buff);
            buff = batch.encode();
        }
        return new RakNetPacket(prep(buff));
    }*/

}
