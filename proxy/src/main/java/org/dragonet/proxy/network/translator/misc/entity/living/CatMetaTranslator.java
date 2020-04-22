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
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractTameableMetaTranslator;

public class CatMetaTranslator extends AbstractTameableMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 18: // Type
                // Variants are not the exact values in Bedrock
                int variantColor;
                switch ((int) metadata.getValue()) {
                    case 0:
                        variantColor = 8;
                        break;
                    case 8:
                        variantColor = 0;
                        break;
                    case 9:
                        variantColor = 10;
                        break;
                    case 10:
                        variantColor = 9;
                        break;
                    default:
                        variantColor = (int) metadata.getValue();
                        break;
                }
                dictionary.putInt(EntityData.VARIANT, variantColor);
                break;
            case 19: // Unknown
            case 20: // Unknown
                break;
            case 21: // Collar color
                if(metadata.getType() == MetadataType.BYTE) {
                    dictionary.putByte(EntityData.COLOR, (byte) (int) metadata.getValue());
                }
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
