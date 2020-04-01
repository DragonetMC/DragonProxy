/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
package org.dragonet.proxy.network.translator.types;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import com.nukkitx.protocol.bedrock.data.EntityFlags;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

@Log4j2
public class EntityMetaTranslator {

    /**
     * This method translates Java entity metadata to Bedrock.
     */
    public static EntityDataMap translateToBedrock(CachedEntity entity, EntityMetadata[] metadata) {
        EntityDataMap dictionary = new EntityDataMap();
        EntityFlags flags = new EntityFlags();

        for(EntityMetadata meta : metadata) {
            switch(meta.getId()) {
                case 0: // Flags
                    byte javaFlags = (byte) meta.getValue();
                    flags.setFlag(EntityFlag.ON_FIRE, (javaFlags & 0x01) > 0);
                    flags.setFlag(EntityFlag.SNEAKING, (javaFlags & 0x02) > 0);
                    flags.setFlag(EntityFlag.SPRINTING, (javaFlags & 0x08) > 0);
                    flags.setFlag(EntityFlag.SWIMMING, (javaFlags & 0x10) > 0);
                    flags.setFlag(EntityFlag.GLIDING, (javaFlags & 0x80) > 0);
                    flags.setFlag(EntityFlag.INVISIBLE, false);

//                    if((javaFlags & 0x20) > 0) { // Invisible
//                        // HACK! Setting the invisible flag will also hide the nametag on bedrock,
//                        // so this hack is needed to simulate invisibility.
//                        dictionary.put(EntityData.SCALE, 0.01f);
//                    } else {
//                        dictionary.put(EntityData.SCALE, entity.getScale());
//                    }
                    break;
                case 1: // Air
                    dictionary.put(EntityData.AIR, meta.getValue());
                    break;
                case 2: // Custom name
                    if(meta.getValue() != null) {
                        dictionary.put(EntityData.NAMETAG, MessageTranslator.translate((Message) meta.getValue()));
                    }
                    break;
                case 3: // Is custom name visible
                    dictionary.put(EntityData.ALWAYS_SHOW_NAMETAG, (boolean) meta.getValue() ? (byte) 1 : (byte) 0);
                    break;
                case 4: // Is silent
                    flags.setFlag(EntityFlag.SILENT, (boolean) meta.getValue());
                    break;
                case 5: // No gravity
                    flags.setFlag(EntityFlag.HAS_GRAVITY, !(boolean) meta.getValue()); // flipped intentionally
                    break;
            }
        }

        dictionary.putFlags(flags);
        return dictionary;
    }
}
