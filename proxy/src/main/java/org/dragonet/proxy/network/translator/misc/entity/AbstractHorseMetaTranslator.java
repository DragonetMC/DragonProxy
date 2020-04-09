/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

import java.util.UUID;

public abstract class AbstractHorseMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 16:
                // 0x02 is tame
                // 0x04 is saddled
                // 0x08 is has bred
                // 0x10 is eating
                // 0x40 is mouth open
                dictionary.getFlags().setFlag(EntityFlag.TAMED, ((byte) metadata.getValue() & 0x02) > 0);
                dictionary.getFlags().setFlag(EntityFlag.SADDLED, ((byte) metadata.getValue() & 0x04) > 0);
                dictionary.getFlags().setFlag(EntityFlag.EATING, ((byte) metadata.getValue() & 0x10) > 0);
                break;
            case 17: // Owner
                if(metadata.getValue() != null) {
                    CachedEntity owner = session.getEntityCache().getByRemoteUUID((UUID) metadata.getValue());
                    long ownerId = owner != null ? owner.getProxyEid() : session.getCachedEntity().getProxyEid();

                    dictionary.putLong(EntityData.OWNER_EID, ownerId);
                }
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
