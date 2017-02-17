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

import java.util.Base64;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.metadata.Pocket101;
import sul.protocol.pocket101.play.PlayStatus;
import sul.protocol.pocket101.play.ResourcePacksInfo;
import sul.protocol.pocket101.play.Respawn;
import sul.protocol.pocket101.play.SetEntityData;
import sul.protocol.pocket101.play.SetSpawnPosition;
import sul.protocol.pocket101.play.StartGame;
import sul.protocol.pocket101.types.BlockPosition;
import sul.protocol.pocket101.types.Pack;
import sul.utils.Tuples;

public class PCJoinGamePacketTranslator implements PCPacketTranslator<ServerJoinGamePacket> {

    @Override
    public sul.utils.Packet[] translate(ClientConnection session, ServerJoinGamePacket packet) {
        //This packet is not fully useable, we cache it for now. 
        session.getDataCache().put(CacheKey.PLAYER_EID, packet.getEntityId());  //Stores the real entity ID
        
        session.getDataCache().put(CacheKey.PACKET_JOIN_GAME_PACKET, packet);

        
        StartGame startGamePacket = new StartGame(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityId = 1;
        startGamePacket.runtimeId = 1;
        //startGamePacket.entityRuntimeId = 52;
        //startGamePacket.x = (float) 0.0;
        //startGamePacket.y = (float) 72F;
        //startGamePacket.z = (float) 0.0;
        //startGamePacket.seed = (int)41568156263L;
        startGamePacket.dimension = (byte) 1 & 0xFF;
        startGamePacket.worldGamemode = 1;
        startGamePacket.difficulty = 1;
        //startGamePacket.spawnX = (int) 0.0;
        //startGamePacket.spawnY = (int) 72;
        //startGamePacket.spawnZ = (int) 0.0;
        //startGamePacket.hasAchievementsDisabled = true;
        //startGamePacket.dayCycleStopTime = -1;
        //startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0;
        //startGamePacket.lightningLevel = 0;
        //startGamePacket.commandsEnabled = true;
        startGamePacket.levelId = Base64.getEncoder().encodeToString("world".getBytes());
        startGamePacket.worldName = "world"; // Must not be null or a NullPointerException will occur
        startGamePacket.generator = 1; //0 old, 1 infinite, 2 flat
        startGamePacket.position = new Tuples.FloatXYZ();
        startGamePacket.spawnPosition = new Tuples.IntXYZ();
        
        /*SetEntityData pkEntityData = new SetEntityData();
        pkEntityData.entityId = 0;
        pkEntityData.metadata = new Pocket101();*/
        
        //Respawn pkResp = new Respawn();
        //pkResp.position = new Tuples.FloatXYZ(0.0f, 72f, 0.0f);

        //SetSpawnPosition pkSpawn = new SetSpawnPosition();
        //pkSpawn.position = new BlockPosition(0, 72, 0); //why 72? keeping it there because it was already there, but seriously. why.
        
        PlayStatus pkStat = new PlayStatus();
        pkStat.status = PlayStatus.SPAWNED;
        
        return new sul.utils.Packet[] {startGamePacket, /*pkEntityData, pkResp, pkSpawn,*/ pkStat};
    }

}
