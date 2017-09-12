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

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import sul.protocol.pocket113.play.PlayerAction;

public class PEPlayerActionPacketTranslator implements PEPacketTranslator<PlayerAction> {

    @Override
    public Packet[] translate(UpstreamSession session, PlayerAction packet) {
        if (packet.action == PlayerAction.RESPAWN) {
            return new Packet[]{new ClientRequestPacket(ClientRequest.RESPAWN)};
        }
        if (packet.action == PlayerAction.START_SPRINT) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SPRINTING);
            return new Packet[]{stat};
        }
        if (packet.action == PlayerAction.STOP_SPRINT) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.STOP_SPRINTING);
            return new Packet[]{stat};
        }
        if (packet.action == PlayerAction.START_SNEAK) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SNEAKING);
            return new Packet[]{stat};
        }
        if (packet.action == PlayerAction.STOP_SNEAK) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.START_SNEAKING);
            return new Packet[]{stat};
        }
        if (packet.action == PlayerAction.STOP_SLEEPING) {
            ClientPlayerStatePacket stat = new ClientPlayerStatePacket((int) session.getDataCache().get(CacheKey.PLAYER_EID), PlayerState.LEAVE_BED);
            return new Packet[]{stat};
        }
        if (packet.action == PlayerAction.DROP_ITEM) {
            ClientPlayerActionPacket act = new ClientPlayerActionPacket(com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.DROP_ITEM, new Position(0, 0, 0), BlockFace.UP);
            return new Packet[]{act};
        }
        if (packet.action == PlayerAction.START_BREAK) {
            ClientPlayerActionPacket act = new ClientPlayerActionPacket(com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.START_DIGGING, new Position(packet.position.x, packet.position.y, packet.position.z), MagicValues.key(BlockFace.class, packet.face));
            session.getDataCache().put(CacheKey.BLOCK_BREAKING_POSITION, act.getPosition());
            return new Packet[]{act};
        }
        if (session.getDataCache().containsKey(CacheKey.BLOCK_BREAKING_POSITION)) {
            if (packet.action == PlayerAction.STOP_BREAK) {
                ClientPlayerActionPacket act = new ClientPlayerActionPacket(com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.FINISH_DIGGING, (Position) session.getDataCache().remove(CacheKey.BLOCK_BREAKING_POSITION), MagicValues.key(BlockFace.class, packet.face));
                return new Packet[]{act};
            }
            if (packet.action == PlayerAction.ABORT_BREAK) {
                ClientPlayerActionPacket act = new ClientPlayerActionPacket(com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction.CANCEL_DIGGING, (Position) session.getDataCache().remove(CacheKey.BLOCK_BREAKING_POSITION), MagicValues.key(BlockFace.class, packet.face));
                return new Packet[]{act};
            }
        }
        return null;
    }

}
