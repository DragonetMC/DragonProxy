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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.AdventureSettings;
import sul.protocol.pocket101.play.MovePlayer;
import sul.protocol.pocket101.play.PlayStatus;
import sul.protocol.pocket101.play.StartGame;
import sul.utils.Tuples;
import sul.utils.Tuples.FloatXYZ;
import sul.utils.Tuples.IntXYZ;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerSpawnPositionPacket packet) {
        if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
            if (DragonProxy.getSelf().getAuthMode().equals("online")) {
            	session.sendChat(DragonProxy.getSelf().getLang().get(Lang.MESSAGE_TELEPORT_TO_SPAWN));
                
                MovePlayer pkMovePlayer = new MovePlayer();
                pkMovePlayer.entityId = 0;
                pkMovePlayer.position = new Tuples.FloatXYZ(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
                pkMovePlayer.headYaw = 0.0f;
                pkMovePlayer.yaw = 0.0f;
                pkMovePlayer.pitch = 0.0f;
                pkMovePlayer.onGround = false;
                pkMovePlayer.animation = MovePlayer.FULL;
                return new sul.utils.Packet[] {pkMovePlayer};
            } else {
                session.getUpstreamProtocol().clientDisconectRequest(session, DragonProxy.getSelf().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return new sul.utils.Packet[0];
        }
        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGame ret = new StartGame();
        ret.entityId = 0; //Use EID 0 for eaisier management
        ret.runtimeId = 0;
        ret.position = new FloatXYZ(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        ret.dimension = (byte) (restored.getDimension() & 0xFF);
        ret.seed = 0;
        ret.generator = 1;
        ret.worldGamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
        ret.spawnPosition = new IntXYZ(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        ret.time = -1;
        ret.rainLevel = 0.0f;
        ret.lightingLevel = 0.0f;
        ret.cheatsEnabled = false;
        ret.worldName = "";

        AdventureSettings adv = new AdventureSettings();
        int settings = 0x1 | 0x20 | 0x40;
        adv.flags = settings;
        
        PlayStatus stat = new PlayStatus();
        stat.status = PlayStatus.SPAWNED;
        
        return new sul.utils.Packet[] {ret, adv, stat};
    }
}
