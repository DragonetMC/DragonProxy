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
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.RespawnPacket;
import org.dragonet.protocol.packets.SetHealthPacket;
import org.dragonet.protocol.packets.UpdateAttributesPacket;

public class PCUpdateHealthPacketTranslator implements IPCPacketTranslator<ServerPlayerHealthPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlayerHealthPacket packet) {

        int newHealth = (int) Math.ceil(packet.getHealth()); // Always round up

        SetHealthPacket h = new SetHealthPacket(newHealth);

        CachedEntity peSelfPlayer = session.getEntityCache().getClientEntity();

        peSelfPlayer.attributes.put(PEEntityAttribute.HEALTH, PEEntityAttribute.findAttribute(PEEntityAttribute.HEALTH).setValue(newHealth));
        if(peSelfPlayer.foodPacketCount==0){
        peSelfPlayer.attributes.put(PEEntityAttribute.FOOD, PEEntityAttribute.findAttribute(PEEntityAttribute.FOOD).setValue(packet.getFood()));
        }
        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.rtid = peSelfPlayer.proxyEid;
        pk.entries = peSelfPlayer.attributes.values();

        if (newHealth <= 0)
            return new PEPacket[]{h, pk, new RespawnPacket()};

        return new PEPacket[]{h, pk};

    }
}
