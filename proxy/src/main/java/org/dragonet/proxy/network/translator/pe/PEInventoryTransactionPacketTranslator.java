package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPEPacketTranslator;
import org.dragonet.proxy.protocol.packets.InventoryTransactionPacket;
import org.dragonet.proxy.protocol.type.InventoryTransactionAction;
import org.dragonet.proxy.utilities.DebugTools;

/**
 * Created on 2017/12/3.
 */
public class PEInventoryTransactionPacketTranslator implements IPEPacketTranslator<InventoryTransactionPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, InventoryTransactionPacket packet) {
        if(packet.type == InventoryTransactionPacket.TYPE_USE_ITEM) {
            return new Packet[] { new ClientPlayerSwingArmPacket(Hand.MAIN_HAND), new ClientPlayerUseItemPacket(Hand.MAIN_HAND) };
        }
        for(InventoryTransactionAction action : packet.actions) {
            System.out.println(DebugTools.getAllFields(action));
        }
        return null;
    }

}
