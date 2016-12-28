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

import org.dragonet.proxy.network.translator.PCPacketTranslator;

import java.util.HashMap;
import java.util.Map;

import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.network.translator.pc.*;
import org.dragonet.proxy.network.translator.pe.*;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateSignPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
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
        PC_TO_PE_TRANSLATOR.put(ServerMultiChunkDataPacket.class, new PCMultiChunkDataPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerChunkDataPacket.class, new PCChunkDataTranslator());
        
        PC_TO_PE_TRANSLATOR.put(ServerUpdateTimePacket.class, new PCUpdateTimePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockChangePacket.class, new PCBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerMultiBlockChangePacket.class, new PCMultiBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateSignPacket.class, new PCUpdateSignPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlaySoundPacket.class, new PCPlaySoundPacketTranslator());

        // Entity
        PC_TO_PE_TRANSLATOR.put(ServerPlayerPositionRotationPacket.class, new PCPlayerPositionRotationPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnMobPacket.class, new PCSpawnMobPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerPlayerListEntryPacket.class, new PCPlayerListItemPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnPlayerPacket.class, new PCSpawnPlayerPacketTranslator());
        //PC_TO_PE_TRANSLATOR.put(ServerSpawnObjectPacket.class, new PCSpawnObjectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityMetadataPacket.class, new PCEntityMetadataPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDestroyEntitiesPacket.class, new PCDestroyEntitiesPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionRotationPacket.class, new PCEntityPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityHeadLookPacket.class, new PCEntityHeadLookPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionPacket.class, new PCEntityPositionPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityVelocityPacket.class, new PCEntityVelocityPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEffectPacket.class, new PCEntityEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRemoveEffectPacket.class, new PCEntityRemoveEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateHealthPacket.class, new PCUpdateHealthPacketTranslator());

        //Inventory
        PC_TO_PE_TRANSLATOR.put(ServerOpenWindowPacket.class, new PCOpenWindowPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerWindowItemsPacket.class, new PCWindowItemsTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSetSlotPacket.class, new PCSetSlotPacketTranslator());
        
    }

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
    }

    public static DataPacket[] translateToPE(UpstreamSession session, Packet packet) {
        if (packet == null) {
            return null;
        }
        
        PCPacketTranslator<Packet> target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if (target == null) {
        	//System.err.println("[PC to PE] No translator found for : " + packet.getClass().getName());
            return null;
        }
        try {
        	//System.out.println(">>> " + packet.getClass().getName());
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
