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
import org.dragonet.proxy.protocol.packet.*;

public final class Protocol {

    private final static HashMap<Byte, Class<? extends PEPacket>> protocol;

    static {
        protocol = new HashMap<>();
        registerDecoder(PEPacketIDs.CLIENT_CONNECT, ClientConnectPacket.class);
        registerDecoder(PEPacketIDs.CLIENT_HANDSHAKE, ClientHandshakePacket.class);
        registerDecoder(PEPacketIDs.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(PEPacketIDs.BATCH_PACKET, BatchPacket.class);
        registerDecoder(PEPacketIDs.TEXT_PACKET, ChatPacket.class);
        registerDecoder(PEPacketIDs.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(PEPacketIDs.DROP_ITEM_PACKET, DropItemPacket.class);
        registerDecoder(PEPacketIDs.LOGIN_PACKET, LoginPacket.class);
        registerDecoder(PEPacketIDs.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerDecoder(PEPacketIDs.MOB_EQUIPMENT_PACKET, PlayerEquipmentPacket.class);
        registerDecoder(PEPacketIDs.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        registerDecoder(PEPacketIDs.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        registerDecoder(PEPacketIDs.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerDecoder(PEPacketIDs.USE_ITEM_PACKET, UseItemPacket.class);
        registerDecoder(PEPacketIDs.WINDOW_SET_SLOT_PACKET, WindowSetSlotPacket.class);
        registerDecoder(PEPacketIDs.INTERACT_PACKET, InteractPacket.class);
        registerDecoder(PEPacketIDs.ENTITY_EVENT_PACKET, EntityEventPacket.class);
    }

    private static void registerDecoder(byte id, Class<? extends PEPacket> clazz) {
        if (protocol.containsKey(id)) {
            return;
        }
        try {
            clazz.getConstructor(byte[].class);
        } catch (NoSuchMethodException | SecurityException ex) {
            return;
        }
        protocol.put(id, clazz);
    }

    public static PEPacket decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        byte pid = data[0];
        if (protocol.containsKey(pid)) {
            Class<? extends PEPacket> c = protocol.get(pid);
            try {
                PEPacket pk = c.getDeclaredConstructor(byte[].class).newInstance((Object) data);
                pk.decode();
                return pk;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            }
        }
        return new UnknownPacket(pid, data);
    }
}
