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
import org.dragonet.common.data.entity.EntityType;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.SetEntityDataPacket;

public class PCEntityMetadataPacketTranslator implements IPCPacketTranslator<ServerEntityMetadataPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityMetadataPacket packet) {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == session.getEntityCache().getClientEntity().eid)
                entity = session.getEntityCache().getClientEntity();
            else
                return null;
//            System.out.println("!!!!!!!!!!!!!!! TRY TO update the player meta from PCEntityMetadataPacketTranslator");
//            return null;
        }

        entity.pcMeta = packet.getMetadata();
        if (entity.spawned) {
            SetEntityDataPacket pk = new SetEntityDataPacket();
            pk.rtid = entity.proxyEid;
            pk.meta = EntityMetaTranslator.translateToPE(session, packet.getMetadata(), entity.peType);
            session.sendPacket(pk);
        } else
            if (entity.peType == EntityType.PLAYER || entity.peType == EntityType.PAINTING) {
                //Do nothing here !
            } else
                entity.spawn(session);
        return null;
    }
}
