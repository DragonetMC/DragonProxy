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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import java.util.ArrayList;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateAttributesPacket;

public class PCSetExperiencePacketTranslator implements IPCPacketTranslator<ServerPlayerSetExperiencePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlayerSetExperiencePacket packet) {

        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.entries = new ArrayList();
        pk.entries.add(new PEEntityAttribute(PEEntityAttribute.EXPERIENCE_LEVEL, "minecraft:player.level", 0.00f, 24791.00f, 0.00f, packet.getLevel()));
        pk.entries.add(new PEEntityAttribute(PEEntityAttribute.EXPERIENCE, "minecraft:player.experience", 0.00f, 1.00f, 0.00f, packet.getSlot()));//weird
        return new PEPacket[]{pk};
    }
}
