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

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Server;
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
import cn.nukkit.network.protocol.ResourcePackClientResponsePacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UseItemPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;

public final class Protocol {
	
	@SuppressWarnings("unchecked")
	private final static Class<? extends DataPacket>[] packetPool = new Class[256];
	static {
		registerPackets();
	}
	
    private static void registerPacket(byte id, Class<? extends DataPacket> clazz) {
        packetPool[id & 0xff] = clazz;
    }
	
    private static void registerPackets() {
    	//registerPacket(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, PEClientConnectPacket.class); // Client Connect 0x09
        registerPacket(ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET, PEClientHandshakePacket.class);
        registerPacket(ProtocolInfo.ANIMATE_PACKET, AnimatePacket.class);
        registerPacket(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        registerPacket(ProtocolInfo.COMMAND_STEP_PACKET, CommandStepPacket.class);
        registerPacket(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        registerPacket(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket.class);
        registerPacket(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);
        registerPacket(ProtocolInfo.DROP_ITEM_PACKET, DropItemPacket.class);
        registerPacket(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);
        registerPacket(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        registerPacket(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket.class);
        registerPacket(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);
        registerPacket(ProtocolInfo.INVENTORY_ACTION_PACKET, InventoryActionPacket.class);
        registerPacket(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket.class);
        registerPacket(ProtocolInfo.LOGIN_PACKET, LoginPacket.class);
        registerPacket(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);
        registerPacket(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        registerPacket(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerPacket(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        registerPacket(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket.class);
        registerPacket(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        registerPacket(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);
        registerPacket(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);
        registerPacket(ProtocolInfo.TEXT_PACKET, TextPacket.class);
        registerPacket(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket.class);
        registerPacket(ProtocolInfo.BATCH_PACKET, BatchPacket.class);
        registerPacket(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class);
    }

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
		        	pk.setBuffer(data, 1);
		        	pk.decode();
		        	return processBatch((BatchPacket) pk);
		    	}
		        
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