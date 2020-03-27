/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import com.nukkitx.protocol.bedrock.packet.BlockPickRequestPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;

@PEPacketTranslator(packetClass = BlockPickRequestPacket.class)
public class PEBlockPickRequestTranslator extends PacketTranslator<BlockPickRequestPacket> {

    @Override
    public void translate(ProxySession session, BlockPickRequestPacket packet) {
        //ItemStack item = session.getChunkCache().getBlockAt(packet.getBlockPosition());
        //session.sendRemotePacket(new ClientCreativeInventoryActionPacket(packet.getHotbarSlot(), item));
    }
}
