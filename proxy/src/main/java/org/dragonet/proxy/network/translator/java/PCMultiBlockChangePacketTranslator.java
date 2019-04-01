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
package org.dragonet.proxy.network.translator.java;

import com.flowpowered.math.vector.Vector3i;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCMultiBlockChangePacketTranslator implements PacketTranslator<ServerMultiBlockChangePacket> {
    public static final PCMultiBlockChangePacketTranslator INSTANCE = new PCMultiBlockChangePacketTranslator();

    @Override
    public void translate(ProxySession session, ServerMultiBlockChangePacket packet) {
        for(BlockChangeRecord record : packet.getRecords()) {
            UpdateBlockPacket updateBlock = new UpdateBlockPacket();

            Position pos = record.getPosition();

            updateBlock.setBlockPosition(new Vector3i(pos.getX(), pos.getY(), pos.getY()));

            session.getUpstream().sendPacket(updateBlock);
        }
    }
}
