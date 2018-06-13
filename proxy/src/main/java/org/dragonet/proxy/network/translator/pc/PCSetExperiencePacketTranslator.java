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
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.UpdateAttributesPacket;

public class PCSetExperiencePacketTranslator implements IPCPacketTranslator<ServerPlayerSetExperiencePacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerPlayerSetExperiencePacket packet) {

        ICachedEntity peSelfPlayer = session.getEntityCache().getClientEntity();

        peSelfPlayer.getAttributes().put(PEEntityAttribute.EXPERIENCE_LEVEL, PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE_LEVEL).setValue(packet.getLevel()));
        peSelfPlayer.getAttributes().put(PEEntityAttribute.EXPERIENCE, PEEntityAttribute.findAttribute(PEEntityAttribute.EXPERIENCE).setValue(packet.getSlot()));

        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.rtid = peSelfPlayer.getProxyEid();
        pk.entries = peSelfPlayer.getAttributes().values();

        return new PEPacket[]{pk};
    }

}
