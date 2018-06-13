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

import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.UpdateAttributesPacket;

public class PCEntityPropertiesPacketTranslator implements IPCPacketTranslator<ServerEntityPropertiesPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerEntityPropertiesPacket packet) {

        ICachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        for (Attribute attr : packet.getAttributes()) {
            switch (attr.getType()) {
                case GENERIC_FOLLOW_RANGE:
                    entity.getAttributes().put(PEEntityAttribute.FOLLOW_RANGE, PEEntityAttribute.findAttribute(PEEntityAttribute.FOLLOW_RANGE).setValue((float) attr.getValue()));
                    break;
                case GENERIC_KNOCKBACK_RESISTANCE:
                    entity.getAttributes().put(PEEntityAttribute.KNOCKBACK_RESISTANCE, PEEntityAttribute.findAttribute(PEEntityAttribute.KNOCKBACK_RESISTANCE).setValue((float) attr.getValue()));
                    break;
                case GENERIC_MOVEMENT_SPEED:
                    entity.getAttributes().put(PEEntityAttribute.MOVEMENT_SPEED, PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED).setValue((float) attr.getValue()));
                    break;
                case GENERIC_ATTACK_DAMAGE:
                    entity.getAttributes().put(PEEntityAttribute.ATTACK_DAMAGE, PEEntityAttribute.findAttribute(PEEntityAttribute.ATTACK_DAMAGE).setValue((float) attr.getValue()));
                    break;
                case GENERIC_FLYING_SPEED:
                    entity.getAttributes().put(PEEntityAttribute.MOVEMENT_SPEED, PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED).setValue((float) attr.getValue()));
                    break;
            }
        }

        if (entity.isSpawned()) {
            UpdateAttributesPacket pk = new UpdateAttributesPacket();
            pk.rtid = entity.getProxyEid();
            pk.entries = entity.getAttributes().values();
            return new PEPacket[]{pk};
        }

        return null;
    }
}
