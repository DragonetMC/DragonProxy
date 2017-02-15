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

import org.dragonet.proxy.protocol.packet.UseItemPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.packetlib.packet.Packet;

public class PEUseItemPacketTranslator implements PEPacketTranslator<UseItemPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, UseItemPacket packet) {
        if (packet.face == 0xFF) {
            //Left click AIR
            ClientSwingArmPacket pk = new ClientSwingArmPacket();
            return new Packet[]{pk};
        }
        ItemStack pcItem = ItemBlockTranslator.translateToPC(packet.item);
        ClientPlayerPlaceBlockPacket pk = new ClientPlayerPlaceBlockPacket(new Position(packet.x, packet.y, packet.z), MagicValues.key(Face.class, packet.face), pcItem, 0.5f, 0.5f, 0.5f);
        return new Packet[]{pk};
    }

}
