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
package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.protocol.packets.BlockPickRequestPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.utilities.DebugTools;

public class PEBlockPickRequestPacketTranslator implements IPEPacketTranslator<BlockPickRequestPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, BlockPickRequestPacket packet) {
        ItemStack item = session.getChunkCache().getBlock(new Position(packet.x, packet.y, packet.z));
        int selectedSlot = (int)session.getDataCache().getOrDefault(CacheKey.PLAYER_SELECTED_SLOT, 36);
        ClientCreativeInventoryActionPacket backPacket = new ClientCreativeInventoryActionPacket(selectedSlot + 36, item);
//        System.out.println("BlockPickRequestPacket " + DebugTools.getAllFields(packet));
//        System.out.println("ItemStack " + DebugTools.getAllFields(item));
        return new Packet[]{backPacket};
    }

}
