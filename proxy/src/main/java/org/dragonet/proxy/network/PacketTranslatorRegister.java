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
import org.spacehq.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerCombatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSetCompressionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSetCooldownPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.packetlib.packet.Packet;

public final class PacketTranslatorRegister {

    private final static Map<Class<? extends Packet>, PCPacketTranslator> PC_TO_PE_TRANSLATOR = new HashMap<>();
    private final static Map<Class<? extends RakNetPacket>, PEPacketTranslator> PE_TO_PC_TRANSLATOR = new HashMap<>();

    /**
     * PC to PE
     */
    static {
        // Login phase
        //PC_TO_PE_TRANSLATOR.put(LoginSuccessPacket.class, new PCLoginSucessPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerJoinGamePacket.class, new PCJoinGamePacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnPositionPacket.class, new PCSpawnPositionPacketTranslator());

        // Settings && Weather
        //PC_TO_PE_TRANSLATOR.put(ServerNotifyClientPacket.class, new PCNotifyClientPacketTranslator());
        // Chat
        //PC_TO_PE_TRANSLATOR.put(ServerChatPacket.class, new PCChatPacketTranslator());

        // Map
        //PC_TO_PE_TRANSLATOR.put(ServerChunkDataPacket.class, new PCChunkDataTranslator());

        //PC_TO_PE_TRANSLATOR.put(ServerUpdateTimePacket.class, new PCUpdateTimePacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerBlockChangePacket.class, new PCBlockChangePacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerMultiBlockChangePacket.class, new PCMultiBlockChangePacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerUpdateTileEntityPacket.class, new PCUpdateSignPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerPlaySoundPacket.class, new PCPlaySoundPacketTranslator());
        // Entity
        //PC_TO_PE_TRANSLATOR.put(ServerPlayerPositionRotationPacket.class, new PCPlayerPositionRotationPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnMobPacket.class, new PCSpawnMobPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerPlayerListEntryPacket.class, new PCPlayerListItemPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnPlayerPacket.class, new PCSpawnPlayerPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnObjectPacket.class, new PCSpawnObjectPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityMetadataPacket.class, new PCEntityMetadataPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityDestroyPacket.class, new PCDestroyEntitiesPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityPositionRotationPacket.class, new PCEntityPositionRotationPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityHeadLookPacket.class, new PCEntityHeadLookPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityPositionPacket.class, new PCEntityPositionPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityVelocityPacket.class, new PCEntityVelocityPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityEffectPacket.class, new PCEntityEffectPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerEntityRemoveEffectPacket.class, new PCEntityRemoveEffectPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerPlayerHealthPacket.class, new PCUpdateHealthPacketTranslator());
        // Inventory
        //PC_TO_PE_TRANSLATOR.put(ServerOpenWindowPacket.class, new PCOpenWindowPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerWindowItemsPacket.class, new PCWindowItemsTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSetSlotPacket.class, new PCSetSlotPacketTranslator());
        // Ignored and unfinished
        PC_TO_PE_TRANSLATOR.put(ServerBossBarPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerCombatPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDifficultyPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDisconnectPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerKeepAlivePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerListDataPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPluginMessagePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerResourcePackSendPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerRespawnPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetCompressionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetCooldownPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerStatisticsPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSwitchCameraPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTabCompletePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTitlePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityAnimationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityAttachPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityCollectItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEquipmentPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityMovementPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPropertiesPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRotationPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntitySetPassengersPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityStatusPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityTeleportPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerVehicleMovePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerAbilitiesPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerChangeHeldItemPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerSetExperiencePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerUseBedPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnExpOrbPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnGlobalEntityPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPaintingPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDisplayScoreboardPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerScoreboardObjectivePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerTeamPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateScorePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerConfirmTransactionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerCloseWindowPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWindowPropertyPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockBreakAnimPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockValuePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerExplosionPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerMapDataPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerOpenTileEntityEditorPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayBuiltinSoundPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayEffectPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnParticlePacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUnloadChunkPacket.class, new IgnorePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWorldBorderPacket.class, new IgnorePacketTranslator());
    }

    /**
     * PE to PC
     */
    static {
        PE_TO_PC_TRANSLATOR.put(RakNetPacket.class, new IgnorePacketTranslator());
    }

    public static RakNetPacket[] translateToPE(ClientConnection session, Packet packet) {
        if (packet == null) {
            return null;
        }

        PCPacketTranslator<Packet> target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            DragonProxy.getLogger().warning("[PC to PE] No translator found for : " + packet.getClass().getName());
            return null;
        }
        try {
            DragonProxy.getLogger().info(">>> " + packet.getClass().getName());
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Packet[] translateToPC(ClientConnection session, RakNetPacket packet) {
        if (packet == null) {
            return null;
        }

        PEPacketTranslator<RakNetPacket> target = PE_TO_PC_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            DragonProxy.getLogger().warning("[PE to PC] No translator found for : " + packet.getClass().getName());
            return null;
        }
        try {
            DragonProxy.getLogger().info("<<< " + packet.getClass().getName());
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
