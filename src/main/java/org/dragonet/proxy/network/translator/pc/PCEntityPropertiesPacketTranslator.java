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
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.UpdateAttributesPacket;

public class PCEntityPropertiesPacketTranslator implements IPCPacketTranslator<ServerEntityPropertiesPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityPropertiesPacket packet) {

        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            return null;
        }
        
        for(Attribute attr : packet.getAttributes())
        {
            switch(attr.getType())
            {
                case GENERIC_FOLLOW_RANGE:
                    entity.attributes.put(PEEntityAttribute.FOLLOW_RANGE, PEEntityAttribute.findAttribute(PEEntityAttribute.FOLLOW_RANGE).setValue((float)attr.getValue()));
                    break;
                case GENERIC_KNOCKBACK_RESISTANCE:
                    entity.attributes.put(PEEntityAttribute.KNOCKBACK_RESISTANCE, PEEntityAttribute.findAttribute(PEEntityAttribute.KNOCKBACK_RESISTANCE).setValue((float)attr.getValue()));
                    break;
                case GENERIC_MOVEMENT_SPEED:
                    entity.attributes.put(PEEntityAttribute.MOVEMENT_SPEED, PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED).setValue((float)attr.getValue()));
                    break;
                case GENERIC_ATTACK_DAMAGE:
                    entity.attributes.put(PEEntityAttribute.ATTACK_DAMAGE, PEEntityAttribute.findAttribute(PEEntityAttribute.ATTACK_DAMAGE).setValue((float)attr.getValue()));
                    break;
                case GENERIC_FLYING_SPEED:
                    entity.attributes.put(PEEntityAttribute.MOVEMENT_SPEED, PEEntityAttribute.findAttribute(PEEntityAttribute.MOVEMENT_SPEED).setValue((float)attr.getValue()));
                    break;
            }
        }

        if (entity.spawned)
        {
            UpdateAttributesPacket pk = new UpdateAttributesPacket();
            pk.rtid = entity.proxyEid;
            pk.entries = entity.attributes.values();
            return new PEPacket[]{pk};
        }
        
        return null;
    }
}
