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

import cn.nukkit.network.protocol.SetSpawnPositionPacket;
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
        
        StartGame startGamePacket = new StartGame(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityId = 0;
        startGamePacket.runtimeId = packet.getEntityId();
        startGamePacket.position = new Tuples.FloatXYZ(0.0f, 0.0f, 0.0f);
        startGamePacket.yaw = 0.0f;
        startGamePacket.pitch = 0.0f;
        startGamePacket.seed = -1;
        startGamePacket.dimension = (byte) packet.getDimension();
        startGamePacket.generator = packet.getWorldType().ordinal(); //0 old, 1 infinite, 2 flat
        startGamePacket.worldGamemode = packet.getGameMode().ordinal();
        startGamePacket.difficulty = packet.getDifficulty().ordinal();
        startGamePacket.spawnPosition = new Tuples.IntXYZ(0, 256, 0);
        startGamePacket.loadedInCreative = packet.getGameMode().ordinal() == GameMode.CREATIVE.ordinal() ? true : false;
        startGamePacket.time = 0;
        startGamePacket.edition = (byte) 0;
        startGamePacket.rainLevel = 0.0f;
        startGamePacket.lightingLevel = 0.0f;
        startGamePacket.cheatsEnabled = true;
        startGamePacket.textureRequired = false;
        startGamePacket.levelId = "";
        startGamePacket.worldName = ""; // Must not be null or a NullPointerException will occur
        
        SetEntityData pkEntityData = new SetEntityData();
        pkEntityData.entityId = 0;
        pkEntityData.metadata = new Pocket100();
        
        Respawn pkResp = new Respawn();
        pkResp.position = new Tuples.FloatXYZ(0.0f, 72f, 0.0f);

        SetSpawnPosition pkSpawn = new SetSpawnPosition();
        pkSpawn.position = new BlockPosition(0, 72, 0); //why 72? keeping it there because it was already there, but seriously. why.
        
        PlayStatus pkStat = new PlayStatus();
        pkStat.status = PlayStatus.SPAWNED;
        
        return new RakNetPacket[]{ 
        		new RakNetPacket(startGamePacket.encode()),
        		new RakNetPacket(pkEntityData.encode()),
        		new RakNetPacket(pkResp.encode()),
        		new RakNetPacket(pkSpawn.encode()),
        		new RakNetPacket(pkStat.encode())
        		};
    }

}
