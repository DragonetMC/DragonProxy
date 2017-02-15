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
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.StartGamePacket;

public class PCJoinGamePacketTranslator implements PCPacketTranslator<ServerJoinGamePacket> {

    @Override
    public DataPacket[] translate(ClientConnection session, ServerJoinGamePacket packet) {
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
        startGamePacket.x = (float) 0.0;
        startGamePacket.y = (float) 0.0;
        startGamePacket.z = (float) 0.0;
        startGamePacket.seed = -1;
        startGamePacket.dimension = (byte) packet.getDimension();
        startGamePacket.gamemode = packet.getGameMode().ordinal();
        startGamePacket.difficulty = packet.getDifficulty().ordinal();
        startGamePacket.spawnX = (int) 0.0;
        startGamePacket.spawnY = (int) 128;
        startGamePacket.spawnZ = (int) 0.0;
        startGamePacket.hasAchievementsDisabled = true;
        startGamePacket.dayCycleStopTime = -1;
        startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0;
        startGamePacket.lightningLevel = 0;
        startGamePacket.commandsEnabled = true;
        startGamePacket.levelId = "";
        startGamePacket.worldName = ""; // Must not be null or a NullPointerException will occur
        startGamePacket.generator = packet.getWorldType().ordinal(); //0 old, 1 infinite, 2 flat
        
/*        SetEntityDataPacket pkEntityData = new SetEntityDataPacket();
        pkEntityData.eid = 0;
        pkEntityData.metadata = new EntityMetadata();
        
        RespawnPacket pkResp = new RespawnPacket();
        pkResp.y = 72F;

        SetSpawnPositionPacket pkSpawn = new SetSpawnPositionPacket();
        pkSpawn.x = 0;
        pkSpawn.y = 72;
        pkSpawn.z = 0;*/
        
        PlayStatusPacket pkStat = new PlayStatusPacket(); //Required; Spawns the client in the world and closes the loading screen
        pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
        
        return new DataPacket[]{ startGamePacket, /*pkEntityData, pkResp, pkSpawn,*/ pkStat };
    }

}
