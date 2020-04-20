/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientUpdateCommandBlockPacket;
import com.nukkitx.protocol.bedrock.packet.CommandBlockUpdatePacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@PacketRegisterInfo(packet = CommandBlockUpdatePacket.class)
public class PECommandBlockUpdateTranslator extends PacketTranslator<CommandBlockUpdatePacket> {

    @Override
    public void translate(ProxySession session, CommandBlockUpdatePacket packet) {
        Position position = new Position(packet.getBlockPosition().getX(), packet.getBlockPosition().getY(), packet.getBlockPosition().getZ());
        CommandBlockMode mode = CommandBlockMode.AUTO;

        if(packet.isRedstoneMode()) {
            mode = CommandBlockMode.REDSTONE;
        }
        else if(packet.isConditional()) {
            mode = CommandBlockMode.SEQUENCE;
        }

        // This is probably right. probably...
        ClientUpdateCommandBlockPacket clientCommandBlockPacket = new ClientUpdateCommandBlockPacket(
            position,
            packet.getCommand(),
            mode,
            packet.isOutputTracked(),
            packet.isConditional(),
            !packet.isRedstoneMode());

        session.sendRemotePacket(clientCommandBlockPacket);
    }
}
