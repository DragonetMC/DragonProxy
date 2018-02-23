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
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import java.util.Arrays;
import org.dragonet.common.maths.ChunkPos;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.AdventureSettingsPacket;
import org.dragonet.protocol.packets.ChangeDimensionPacket;
import org.dragonet.protocol.packets.FullChunkDataPacket;
import org.dragonet.protocol.packets.PlayStatusPacket;
import org.dragonet.protocol.packets.RemoveEntityPacket;
import org.dragonet.protocol.packets.SetDifficultyPacket;
import org.dragonet.protocol.packets.SetPlayerGameTypePacket;
import org.dragonet.protocol.type.chunk.ChunkData;
import org.dragonet.protocol.type.chunk.Section;
import org.dragonet.proxy.DragonProxy;

public class PCRespawnPacketTranslator implements IPCPacketTranslator<ServerRespawnPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerRespawnPacket packet) {

        CachedEntity entity = session.getEntityCache().getClientEntity();
        if (entity.dimention != packet.getDimension()) {
            // the player have changed dimention
            DragonProxy.getInstance().getLogger().info(session.getUsername() + " change dim " + entity.dimention + " to " + packet.getDimension());

            // remove all entities
            CachedEntity[] cachedEntity = session.getEntityCache().getEntities().values().toArray(new CachedEntity[session.getEntityCache().getEntities().size()]);
            RemoveEntityPacket[] removeEntities = new RemoveEntityPacket[cachedEntity.length - 1];
            for (int i = 0; i < removeEntities.length; i++) {
                if (cachedEntity[i] == null || cachedEntity[i].proxyEid == 1L)
                    continue;
                removeEntities[i] = new RemoveEntityPacket();
                removeEntities[i].eid = cachedEntity[i].proxyEid;
            }
            session.sendAllPackets(removeEntities, true);
            session.getEntityCache().reset(true);

            // reseting world chunks
//            ChunkPos[] chunkPos = session.getChunkCache().getLoadedChunks().toArray(new ChunkPos[session.getChunkCache().getLoadedChunks().size()]);
//            FullChunkDataPacket[] emptyChunks = new FullChunkDataPacket[chunkPos.length];
//            ChunkData data = new ChunkData();
//            data.sections = new Section[16];
//            for (int cy = 0; cy < 16; cy++) {
//                data.sections[cy] = new Section();
//                if (cy < 6)
//                    Arrays.fill(data.sections[cy].blockIds, (byte) 0);
//            }
//            data.encode();
//            for (int i = 0; i < emptyChunks.length; i++) {
//                emptyChunks[i] = new FullChunkDataPacket();
//                emptyChunks[i].payload = data.getBuffer();
//                emptyChunks[i].x = chunkPos[i].chunkXPos;
//                emptyChunks[i].z = chunkPos[i].chunkZPos;
//            }
//            session.sendAllPackets(emptyChunks, true);
//            session.getChunkCache().clear();

            // send new world gamemode
            SetPlayerGameTypePacket pkgm = new SetPlayerGameTypePacket();
            pkgm.gamemode = packet.getGameMode() == GameMode.CREATIVE ? 1 : 0;
            session.sendPacket(pkgm);

            // send new adventure settings
            AdventureSettingsPacket adv = new AdventureSettingsPacket();
            adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, packet.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, packet.getGameMode().equals(GameMode.CREATIVE) || packet.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.NO_CLIP, packet.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.WORLD_BUILDER, !packet.getGameMode().equals(GameMode.SPECTATOR) || !packet.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.FLYING, packet.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.MUTED, false);
            adv.eid = entity.proxyEid;
            adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;
            adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER;
            session.sendPacket(adv);

            //set world difficulty
            session.sendPacket(new SetDifficultyPacket(packet.getDifficulty()));

            // change dim packet
            ChangeDimensionPacket changeDimPacket = new ChangeDimensionPacket();
            changeDimPacket.dimension = packet.getDimension();
            changeDimPacket.position = entity.spawnPosition.toVector3F();
            changeDimPacket.respawn = false;
            session.sendPacket(new ChangeDimensionPacket());

            return null;
        } else
            return new PEPacket[]{new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN)};
    }
}
