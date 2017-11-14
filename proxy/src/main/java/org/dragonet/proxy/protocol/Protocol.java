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

import org.dragonet.proxy.protocol.packets.*;
import org.dragonet.proxy.utilities.BinaryStream;
import org.dragonet.proxy.utilities.Zlib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.dragonet.proxy.protocol.ProtocolInfo.*;

public final class Protocol {

    public final static Map<Byte, Class<? extends PEPacket>> packets = new HashMap<>();

    static {
        packets.put(DISCONNECT_PACKET, DisconnectPacket.class);
        packets.put(LOGIN_PACKET, LoginPacket.class);
        packets.put(PLAY_STATUS_PACKET, PlayStatusPacket.class);
        packets.put(START_GAME_PACKET, StartGamePacket.class);
        packets.put(REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket.class);
        packets.put(CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket.class);
        packets.put(FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        packets.put(UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        packets.put(TEXT_PACKET, TextPacket.class);
        packets.put(REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        packets.put(MOB_EFFECT_PACKET, MobEffectPacket.class);
        packets.put(ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        packets.put(MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        packets.put(MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        packets.put(SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        packets.put(SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        packets.put(ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        packets.put(SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);
        packets.put(LEVEL_EVENT_PACKET, LevelEventPacket.class);
        packets.put(PLAY_SOUND_PACKET, PlaySoundPacket.class);
        packets.put(ADD_ENTITY_PACKET, AddEntityPacket.class);
        packets.put(ADD_PLAYER_PACKET, AddPlayerPacket.class);
        packets.put(PLAYER_LIST_PACKET, PlayerListPacket.class);
        packets.put(SET_HEALTH_PACKET, SetHealthPacket.class);
        packets.put(RESPAWN_PACKET, RespawnPacket.class);
        packets.put(BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        packets.put(SET_TIME_PACKET, SetTimePacket.class);
        packets.put(INTERACT_PACKET, InteractPacket.class);
        packets.put(PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        packets.put(MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        packets.put(SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);

        packets.put(CONTAINER_OPEN_PACKET, ContainerOpenPacket.class);
        packets.put(CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        packets.put(INVENTORY_CONTENT_PACKET, InventoryContentPacket.class);
        packets.put(INVENTORY_SLOT_PACKET, InventorySlotPacket.class);

        packets.put(RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
        packets.put(RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class);
        packets.put(RESOURCE_PACK_STACK_PACKET, ResourcePackStackPacket.class);
    }

    public static PEPacket[] decode(byte[] data) throws Exception {
        if (data == null || data.length < 1) {
            return null;
        }

        byte[] inflated;
        try {
            inflated = Zlib.inflate(Arrays.copyOfRange(data, 1, data.length));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ArrayList<PEPacket> packets = new ArrayList<>(2);
        BinaryStream stream = new BinaryStream(inflated);
        while(stream.offset < inflated.length) {
            byte[] buffer = stream.get((int)stream.getUnsignedVarInt());
            PEPacket decoded = decodeSingle(buffer);

            if(decoded != null) {
                packets.add(decoded);
            } else {
                System.out.println("decode fail");
            }
        }

        return packets.size() > 0 ? packets.toArray(new PEPacket[0]) : null;
    }

    private static PEPacket decodeSingle(byte[] buffer) {
        try{
            FileOutputStream fos = new FileOutputStream("raw_" + System.currentTimeMillis() + ".bin");
            fos.write(buffer);
            fos.close();
        }catch(Exception e){}
        byte pid = (byte) new BinaryStream(buffer).getUnsignedVarInt();
        if (packets.containsKey(pid)) {
            Class<? extends PEPacket> c = packets.get(pid);
            try {
                PEPacket pk = c.newInstance();
                pk.setBuffer(buffer);
                pk.decode();
                return pk;
            } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("can not decode for pid 0x" + Integer.toHexString(pid));
        }
        return null;
    }
}
