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
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.packets.AdventureSettingsPacket;
import org.dragonet.common.mcbedrock.protocol.packets.PlayStatusPacket;
import org.dragonet.common.mcbedrock.protocol.packets.SetDifficultyPacket;
import org.dragonet.common.mcbedrock.protocol.packets.SetPlayerGameTypePacket;

public class PCRespawnPacketTranslator implements IPCPacketTranslator<ServerRespawnPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerRespawnPacket packet) {

        CachedEntity entity = session.getEntityCache().getClientEntity();
        if (entity.dimention != packet.getDimension())
        {
//            DragonProxy.getInstance().getLogger().debug(session.getUsername() + " change dim " + entity.dimention + " to " + packet.getDimension());
            
            session.getEntityCache().reset(true);

            SetPlayerGameTypePacket pkgm = new SetPlayerGameTypePacket();
            pkgm.gamemode = packet.getGameMode() == GameMode.CREATIVE ? 1 : 0;

            AdventureSettingsPacket adv = new AdventureSettingsPacket();
            adv.setFlag(AdventureSettingsPacket.WORLD_IMMUTABLE, packet.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.ALLOW_FLIGHT, packet.getGameMode().equals(GameMode.CREATIVE) || packet.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.NO_CLIP, packet.getGameMode().equals(GameMode.SPECTATOR));
            adv.setFlag(AdventureSettingsPacket.WORLD_BUILDER, !packet.getGameMode().equals(GameMode.SPECTATOR) || !packet.getGameMode().equals(GameMode.ADVENTURE));
            adv.setFlag(AdventureSettingsPacket.FLYING, false);
            adv.setFlag(AdventureSettingsPacket.MUTED, false);
            adv.eid = entity.proxyEid;
            adv.commandsPermission = AdventureSettingsPacket.PERMISSION_NORMAL;
            adv.playerPermission = AdventureSettingsPacket.LEVEL_PERMISSION_MEMBER;

            return new PEPacket[] {
                new SetDifficultyPacket(packet.getDifficulty()),
                pkgm,
                adv
            };
        }
        else
        {
            return new PEPacket[] {new PlayStatusPacket(PlayStatusPacket.PLAYER_SPAWN)};
        }
    }
}
