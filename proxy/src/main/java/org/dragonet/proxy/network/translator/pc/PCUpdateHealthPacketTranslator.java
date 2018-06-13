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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.RespawnPacket;
import org.dragonet.protocol.packets.SetHealthPacket;
import org.dragonet.protocol.packets.UpdateAttributesPacket;

public class PCUpdateHealthPacketTranslator implements IPCPacketTranslator<ServerPlayerHealthPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerPlayerHealthPacket packet) {

        int newHealth = (int) Math.ceil(packet.getHealth()); // Always round up

        SetHealthPacket h = new SetHealthPacket(newHealth);

        ICachedEntity peSelfPlayer = session.getEntityCache().getClientEntity();

        peSelfPlayer.getAttributes().put(PEEntityAttribute.HEALTH, PEEntityAttribute.findAttribute(PEEntityAttribute.HEALTH).setValue(newHealth));
        if (peSelfPlayer.getFoodPacketCount() == 0)
            peSelfPlayer.getAttributes().put(PEEntityAttribute.FOOD, PEEntityAttribute.findAttribute(PEEntityAttribute.FOOD).setValue(packet.getFood()));
        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.rtid = peSelfPlayer.getProxyEid();
        pk.entries = peSelfPlayer.getAttributes().values();

        if (newHealth <= 0)
            return new PEPacket[]{h, pk, new RespawnPacket()};

        return new PEPacket[]{h, pk};

    }
}
