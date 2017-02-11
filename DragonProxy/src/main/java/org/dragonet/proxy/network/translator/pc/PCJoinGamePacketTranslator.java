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

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;

import cn.nukkit.network.protocol.StartGamePacket;
import net.marfgamer.jraknet.RakNetPacket;
import sul.metadata.Pocket100;
import sul.protocol.pocket100.play.PlayStatus;
import sul.protocol.pocket100.play.Respawn;
import sul.protocol.pocket100.play.SetEntityData;
import sul.protocol.pocket100.play.SetSpawnPosition;
import sul.protocol.pocket100.play.StartGame;
import sul.protocol.pocket100.types.BlockPosition;
import sul.utils.Tuples;

public class PCJoinGamePacketTranslator implements PCPacketTranslator<ServerJoinGamePacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerJoinGamePacket packet) {
        //This packet is not fully useable, we cache it for now. 
        session.getDataCache().put(CacheKey.PLAYER_EID, packet.getEntityId());  //Stores the real entity ID

/*        SetPlayerGameTypePacket pkSetGameMode = new SetPlayerGameTypePacket();
        pkSetGameMode.gamemode = packet.getGameMode().ordinal();*/
        
        session.getDataCache().put(CacheKey.PACKET_JOIN_GAME_PACKET, packet);
        
        //packet.getHardcore(); Unsupported by Minecraft PE as of 1.0.0
        //packet.getMaxPlayers(); Unsupported by Minecraft PE as of 1.0.0
        //packet.getReducedDebugInfo(); Unsupported by Minecraft PE as of 1.0.0
        
        StartGamePacket startGamePacket = new StartGamePacket(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityUniqueId = 0;
        startGamePacket.entityRuntimeId = packet.getEntityId();
        startGamePacket.x = 0.0f;
        startGamePacket.y = 0.0f;
        startGamePacket.z = 0.0f;
        startGamePacket.seed = -1;
        startGamePacket.dimension = (byte) packet.getDimension();
        startGamePacket.generator = packet.getWorldType().ordinal(); //0 old, 1 infinite, 2 flat
        startGamePacket.gamemode = packet.getGameMode().ordinal();
        startGamePacket.difficulty = packet.getDifficulty().ordinal();
        startGamePacket.spawnX = 0;
        startGamePacket.spawnY = 256;
        startGamePacket.spawnZ = 0;
        startGamePacket.dayCycleStopTime = -1;
        startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0.0f;
        startGamePacket.lightningLevel = 0.0f;
        startGamePacket.commandsEnabled = true;
        startGamePacket.isTexturePacksRequired = false;
        startGamePacket.levelId = "d29ybGQ="; //"world" in base64
        startGamePacket.worldName = "world"; // Must not be null or a NullPointerException will occur
        
        SetEntityData pkEntityData = new SetEntityData();
        pkEntityData.entityId = 0;
        pkEntityData.metadata = new Pocket100();
        
        Respawn pkResp = new Respawn();
        pkResp.position = new Tuples.FloatXYZ(0.0f, 72f, 0.0f);

        SetSpawnPosition pkSpawn = new SetSpawnPosition();
        pkSpawn.position = new BlockPosition(0, 72, 0); //why 72? keeping it there because it was already there, but seriously. why.
        
        PlayStatus pkStat = new PlayStatus();
        pkStat.status = PlayStatus.SPAWNED;
        
        return fromSulPackets(startGamePacket, pkEntityData, pkResp, pkSpawn, pkStat);
    }

}
