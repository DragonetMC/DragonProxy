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
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateAttributesPacket;

public class PCSetExperiencePacketTranslator implements IPCPacketTranslator<ServerPlayerSetExperiencePacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlayerSetExperiencePacket packet) {

        CachedEntity peSelfPlayer = session.getEntityCache().getClientEntity();

        peSelfPlayer.attributes.put(PEEntityAttribute.EXPERIENCE_LEVEL, PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL).setValue(packet.getLevel()));
        peSelfPlayer.attributes.put(PEEntityAttribute.EXPERIENCE, PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE).setValue(packet.getSlot()));

        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.rtid = peSelfPlayer.proxyEid;
        pk.entries = peSelfPlayer.attributes.values();

        return new PEPacket[]{pk};
    }

}
