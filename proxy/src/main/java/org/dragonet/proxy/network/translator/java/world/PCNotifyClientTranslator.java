/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java.world;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import com.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

import java.util.concurrent.ThreadLocalRandom;

public class PCNotifyClientTranslator implements PacketTranslator<ServerNotifyClientPacket> {
    public static final PCNotifyClientTranslator INSTANCE = new PCNotifyClientTranslator();

    @Override
    public void translate(ProxySession session, ServerNotifyClientPacket packet) {
        switch(packet.getNotification()) {
            case CHANGE_GAMEMODE:
                SetPlayerGameTypePacket setGameTypePacket = new SetPlayerGameTypePacket();
                setGameTypePacket.setGamemode(MagicValues.value(Integer.class, packet.getValue()));

                session.getBedrockSession().sendPacket(setGameTypePacket);
        }
    }
}
