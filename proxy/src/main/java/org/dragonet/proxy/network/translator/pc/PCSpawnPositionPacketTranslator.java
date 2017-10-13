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
import sul.protocol.bedrock137.play.*;
import sul.protocol.bedrock137.types.ChunkData;
import sul.protocol.bedrock137.types.Section;
import sul.utils.Packet;
import sul.utils.Tuples;

import java.util.Arrays;

public class PCSpawnPositionPacketTranslator implements PCPacketTranslator<ServerSpawnPositionPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerSpawnPositionPacket packet) {
        if (session.getDataCache().get(CacheKey.PACKET_JOIN_GAME_PACKET) == null) {
            if (session.getProxy().getAuthMode().equals("online")) {
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_TELEPORT_TO_SPAWN));
                MovePlayer pkMovePlayer = new MovePlayer();
                pkMovePlayer.position = new Tuples.FloatXYZ((float) packet.getPosition().getX(), (float) packet.getPosition().getY(), (float) packet.getPosition().getZ());
                pkMovePlayer.animation = (byte) 0;
                pkMovePlayer.onGround = true;
                return new Packet[]{pkMovePlayer};
            } else {
                session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_REMOTE_ERROR));
            }
            return null;
        }

        ServerJoinGamePacket restored = (ServerJoinGamePacket) session.getDataCache().remove(CacheKey.PACKET_JOIN_GAME_PACKET);
        StartGame ret = new StartGame();
        ret.entityId = 0; //Use EID 0 for eaisier management
        ret.dimension = (byte) (restored.getDimension() & 0xFF);
        ret.seed = 0;
        ret.generator = 1;
        ret.gamemode = restored.getGameMode() == GameMode.CREATIVE ? 1 : 0;
        ret.spawnPosition = new Tuples.IntXYZ(0, 0, 0); // new Tuples.IntXYZ(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ());
        ret.position = new Tuples.FloatXYZ(0f, 0f, 0f);// new Tuples.FloatXYZ((float) packet.getPosition().getX(), (float) packet.getPosition().getY(), (float) packet.getPosition().getZ());
        ret.levelId = "";
        ret.worldName = "World";
        ret.premiumWorldTemplate = "";
        AdventureSettings adv = new AdventureSettings();
        int settings = 0x1 | 0x20 | 0x40;
        adv.flags = settings;

        session.sendPacket(ret);
        session.sendPacket(new ResourcePacksInfo());
        session.sendPacket(adv);

        // test things: we send empty chunks first
        ChunkData test_chunk = new ChunkData();
        test_chunk.sections = new Section[16];
        test_chunk.sections[0] = new Section();
        Arrays.fill(test_chunk.sections[0].blockIds, (byte)1);
        for(int cx = -6; cx < 7; cx++) {
            for(int cz = -6; cz < 7; cz++) {
                session.sendPacket(new FullChunkData(new Tuples.IntXZ(cx, cz), test_chunk));
            }
        }


        session.getEntityCache().getClientEntity().x = packet.getPosition().getX();
        session.getEntityCache().getClientEntity().y = packet.getPosition().getY();
        session.getEntityCache().getClientEntity().z = packet.getPosition().getZ();

        return null; // return new Packet[]{ret, new ResourcePacksInfo(), adv};
    }

}
