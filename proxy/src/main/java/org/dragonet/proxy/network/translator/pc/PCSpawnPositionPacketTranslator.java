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

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.AdventureSettingsPacket;
import org.dragonet.proxy.protocol.packets.MovePlayerPacket;
import org.dragonet.proxy.protocol.packets.ResourcePacksInfoPacket;
import org.dragonet.proxy.protocol.packets.StartGamePacket;
import org.dragonet.proxy.utilities.BlockPosition;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnPositionPacket packet) {
        if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
            if (session.getProxy().getAuthMode().equals("online")) {
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_TELEPORT_TO_SPAWN));
                MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
                pkMovePlayer.position = new Vector3F((float) packet.getPosition().getX(), (float) packet.getPosition().getY(), (float) packet.getPosition().getZ());
                pkMovePlayer.mode = MovePlayerPacket.MODE_TELEPORT;
                pkMovePlayer.onGround = true;
                return new PEPacket[]{pkMovePlayer};
            } else {
                session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return null;
        }

        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGamePacket ret = new StartGamePacket();
        ret.rtid = 0;
        ret.eid = 0; //Use EID 0 for eaisier management
        ret.dimension = (byte) (restored.getDimension() & 0xFF);
        ret.seed = 0;
        ret.generator = 1;
        ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
        ret.spawnPosition = new BlockPosition(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        ret.position = new Vector3F((float) packet.getPosition().getX(), (float) packet.getPosition().getY(), (float) packet.getPosition().getZ());
        ret.levelId = "World";
        ret.worldName = "World";
        ret.premiumWorldTemplateId = "";
        AdventureSettingsPacket adv = new AdventureSettingsPacket();
        adv.setFlag(AdventureSettingsPacket.AUTO_JUMP, true);
        adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, true);

        session.getEntityCache().getClientEntity().x = packet.getPosition().getX();
        session.getEntityCache().getClientEntity().y = packet.getPosition().getY();
        session.getEntityCache().getClientEntity().z = packet.getPosition().getZ();

        return new PEPacket[]{ret, new ResourcePacksInfoPacket(), adv};
    }

}
