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

import org.dragonet.proxy.protocol.packet.LoginStatusPacket;
import org.dragonet.proxy.protocol.packet.MovePlayerPacket;
import org.dragonet.proxy.protocol.packet.StartGamePacket;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.protocol.packet.AdventureSettingsPacket;
import com.github.steveice10.mc.protocol.data.game.values.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnPositionPacket packet) {
        if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
            if (session.getProxy().getAuthMode().equals("online")) {
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_TELEPORT_TO_SPAWN));
                MovePlayerPacket pkMovePlayer = new MovePlayerPacket(0, (float) packet.getPosition().getX(), (float) packet.getPosition().getY(), (float) packet.getPosition().getZ(), 0.0f, 0.0f, 0.0f, false);
                pkMovePlayer.mode = MovePlayerPacket.MODE_RESET;
                return new PEPacket[]{pkMovePlayer};
            } else {
                session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return null;
        }
        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGamePacket ret = new StartGamePacket();
        ret.eid = 0; //Use EID 0 for eaisier management
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

        AdventureSettingsPacket adv = new AdventureSettingsPacket();
        int settings = 0x1 | 0x20 | 0x40;
        adv.flags = settings;
        
        LoginStatusPacket stat = new LoginStatusPacket();
        stat.status = LoginStatusPacket.PLAYER_SPAWN;
        return new PEPacket[]{ret, adv, stat};
    }

}
