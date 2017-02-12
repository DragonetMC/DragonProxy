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
package org.dragonet.proxy.network.translator.pe;

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.ClientRequest;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;
import org.spacehq.mc.protocol.data.game.entity.player.PlayerAction;
import org.spacehq.mc.protocol.data.game.entity.player.PlayerState;
import org.spacehq.mc.protocol.data.game.world.block.BlockFace;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import org.spacehq.packetlib.packet.Packet;

public class PEPlayerActionPacketTranslator implements PEPacketTranslator<sul.protocol.pocket101.play.PlayerAction> {

    @Override
    public Packet[] translate(ClientConnection session, sul.protocol.pocket101.play.PlayerAction packet) {
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.RESPAWN) {
            return new Packet[]{new ClientRequestPacket(ClientRequest.RESPAWN)};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.START_SPRINT) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SPRINTING);
            return new Packet[]{stat};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.STOP_SPRINT) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.STOP_SPRINTING);
            return new Packet[]{stat};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.START_SNEAK) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SNEAKING);
            return new Packet[]{stat};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.STOP_SNEAK) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SNEAKING);
            return new Packet[]{stat};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.STOP_SLEEPING) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.LEAVE_BED);
            return new Packet[]{stat};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.RELEASE_ITEM) {
            ClientPlayerActionPacket act = new ClientPlayerActionPacket(PlayerAction.DROP_ITEM, new Position(0, 0, 0), BlockFace.UP);
            return new Packet[]{act};
        }
        if (packet.action == sul.protocol.pocket101.play.PlayerAction.START_BREAK) {
            ClientPlayerActionPacket act = new ClientPlayerActionPacket(PlayerAction.START_DIGGING, new Position(packet.position.x, packet.position.y, packet.position.z), MagicValues.key(BlockFace.class, packet.face));
            session.getDataCache().put(CacheKey.BLOCK_BREAKING_POSITION, act.getPosition());
            return new Packet[]{act};
        }
        if (session.getDataCache().containsKey(CacheKey.BLOCK_BREAKING_POSITION)) {
            if (packet.action == sul.protocol.pocket101.play.PlayerAction.STOP_BREAK) {
                ClientPlayerActionPacket act = new ClientPlayerActionPacket(PlayerAction.FINISH_DIGGING, (Position) session.getDataCache().remove(CacheKey.BLOCK_BREAKING_POSITION), MagicValues.key(BlockFace.class, packet.face));
                return new Packet[]{act};
            }
            if (packet.action == sul.protocol.pocket101.play.PlayerAction.ABORT_BREAK) {
                ClientPlayerActionPacket act = new ClientPlayerActionPacket(PlayerAction.CANCEL_DIGGING, (Position) session.getDataCache().remove(CacheKey.BLOCK_BREAKING_POSITION), MagicValues.key(BlockFace.class, packet.face));
                return new Packet[]{act};
            }
        }
        return null;
    }

}
