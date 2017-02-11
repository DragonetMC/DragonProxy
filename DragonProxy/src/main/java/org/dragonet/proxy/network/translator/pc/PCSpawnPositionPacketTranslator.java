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

import cn.nukkit.network.protocol.StartGamePacket;
import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket100.play.AdventureSettings;
import sul.protocol.pocket100.play.MovePlayer;
import sul.protocol.pocket100.play.PlayStatus;
import sul.utils.Tuples;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerSpawnPositionPacket packet) {
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
                return fromSulPackets(pkMovePlayer);
            } else {
                session.getUpstreamProtocol().clientDisconectRequest(session, DragonProxy.getSelf().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return null;
        }
        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGamePacket ret = new StartGamePacket();
        ret.entityUniqueId = 0; //Use EID 0 for eaisier management
        ret.entityRuntimeId = 0;
        ret.x = packet.getPosition().getX();
        ret.y = packet.getPosition().getY();
        ret.z = packet.getPosition().getZ();
        ret.dimension = (byte) (restored.getDimension() & 0xFF);
        ret.seed = 0;
        ret.generator = 1;
        ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
        ret.spawnX = packet.getPosition().getX();
        ret.spawnY = packet.getPosition().getY();
        ret.spawnZ = packet.getPosition().getZ();
        ret.dayCycleStopTime = -1;
        ret.rainLevel = 0.0f;
        ret.lightningLevel = 0.0f;
        ret.commandsEnabled = false;
        ret.worldName = "";

        AdventureSettings adv = new AdventureSettings();
        int settings = 0x1 | 0x20 | 0x40;
        adv.flags = settings;
        
        PlayStatus stat = new PlayStatus();
        stat.status = PlayStatus.SPAWNED;
        
        return fromSulPackets(ret, adv, stat);
    }
}
