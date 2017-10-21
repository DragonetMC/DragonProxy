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
        packets.put(FULL_CHUNK_DATA_PACKET, FullChunkDataPacket.class);
        packets.put(UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        packets.put(TEXT_PACKET, TextPacket.class);
        packets.put(REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        packets.put(MOB_EFFECT_PACKET, MobEffectPacket.class);
        packets.put(ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        packets.put(MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        packets.put(SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        packets.put(SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        packets.put(ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket.class);
        packets.put(SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket.class);

        packets.put(INVENTORY_CONTENT_PACKET, InventoryContentPacket.class);
        packets.put(INVENTORY_SLOT_PACKET, InventorySlotPacket.class);

        packets.put(RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket.class);
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
        int pid = buffer[0] & 0xFF;
        if (packets.containsKey(pid)) {
            Class<? extends PEPacket> c = packets.get(pid);
            try {
                PEPacket pk = c.newInstance();
                pk.setBuffer(buffer);
                pk.decode();
                System.out.println(pk.getClass().getSimpleName());
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
