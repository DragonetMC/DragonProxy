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

import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCChunkDataTranslator implements PacketTranslator<ServerChunkDataPacket> {
    public static final PCChunkDataTranslator INSTANCE = new PCChunkDataTranslator();

    @Override
    public void translate(ProxySession session, ServerChunkDataPacket packet) {
        Column column = packet.getColumn();
        LevelChunkPacket fullChunkData = new LevelChunkPacket();
        fullChunkData.setChunkX(column.getX());
        fullChunkData.setChunkZ(column.getZ());
        fullChunkData.setData(new byte[0]);

        // TODO: 01/04/2019 Finish off chunk data

        //session.getBedrockSession().sendPacket(fullChunkData);
    }
}
