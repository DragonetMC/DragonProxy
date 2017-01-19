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

import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;

import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.StartGamePacket;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public DataPacket[] translate(UpstreamSession session, ServerSpawnPositionPacket packet) {
        if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
            if (session.getProxy().getAuthMode().equals("online")) {
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_TELEPORT_TO_SPAWN));
                
                MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
                pkMovePlayer.eid = 0;
                pkMovePlayer.x = packet.getPosition().getX();
                pkMovePlayer.y = packet.getPosition().getY();
                pkMovePlayer.z = packet.getPosition().getZ();
                pkMovePlayer.headYaw = 0.0f;
                pkMovePlayer.yaw = 0.0f;
                pkMovePlayer.pitch = 0.0f;
                pkMovePlayer.onGround = false;
                pkMovePlayer.mode = MovePlayerPacket.MODE_RESET;
                return new DataPacket[]{pkMovePlayer};
            } else {
                session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return null;
        }
        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGamePacket ret = new StartGamePacket();
        ret.entityRuntimeId = 0; //Use EID 0 for eaisier management
        ret.dimension = (byte) (restored.getDimension() & 0xFF);
        ret.seed = 0;
        ret.generator = 1;
        ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
        ret.spawnX = packet.getPosition().getX();
        ret.spawnY = packet.getPosition().getY();
        ret.spawnZ = packet.getPosition().getZ();
        ret.x = packet.getPosition().getX();
        ret.y = packet.getPosition().getY();
        ret.z = packet.getPosition().getZ();
        ret.worldName = "";

        AdventureSettingsPacket adv = new AdventureSettingsPacket();
        int settings = 0x1 | 0x20 | 0x40;
        adv.flags = settings;
        
        PlayStatusPacket stat = new PlayStatusPacket();
        stat.status = PlayStatusPacket.PLAYER_SPAWN;
        return new DataPacket[]{ret, adv, stat};
    }

}
