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

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.common.data.entity.EntityType;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.SetEntityDataPacket;

public class PCEntityMetadataPacketTranslator implements IPCPacketTranslator<ServerEntityMetadataPacket> {

    public PEPacket[] translate(IUpstreamSession session, ServerEntityMetadataPacket packet) {
        ICachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == session.getEntityCache().getClientEntity().getEid())
                entity = session.getEntityCache().getClientEntity();
            else
                return null;
//            System.out.println("!!!!!!!!!!!!!!! TRY TO update the player meta from PCEntityMetadataPacketTranslator");
//            return null;
        }

        entity.setPcMeta(packet.getMetadata());
        if (entity.isSpawned()) {
            SetEntityDataPacket pk = new SetEntityDataPacket();
            pk.rtid = entity.getProxyEid();
            pk.meta = EntityMetaTranslator.translateToPE(session, packet.getMetadata(), entity.getPeType());
            session.sendPacket(pk);
        } else
            if (entity.getPeType() == EntityType.PLAYER || entity.getPeType() == EntityType.PAINTING) {
                //Do nothing here !
            } else
                entity.spawn(session);
        return null;
    }
}
