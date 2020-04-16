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
package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityEventType;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

@Log4j2
public class EnderDragonMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if (metadata.getId() == 15) { // Dragon phase
            switch ((int) metadata.getValue()) {
                case 5: // Landed, performing breath attack
                    EntityEventPacket entityEventPacket = new EntityEventPacket();
                    entityEventPacket.setRuntimeEntityId(entity.getProxyEid());
                    entityEventPacket.setType(EntityEventType.DRAGON_FLAMING);
                    entityEventPacket.setData(0);

                    session.sendPacket(entityEventPacket);
                    break;
                case 6: // Landed, looking for a player for breath attack
                case 7: // Landed, roar before beginning breath attack
                    dictionary.getFlags().setFlag(EntityFlag.SITTING, true);
                    break;
                case 9:
                    // Handle death here?
                    break;
                case 10: // Hovering with no AI
                    dictionary.getFlags().setFlag(EntityFlag.NO_AI, true);
                    break;
            }
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
