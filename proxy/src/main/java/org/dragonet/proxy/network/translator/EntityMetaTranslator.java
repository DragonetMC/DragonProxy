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
package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.entity.meta.type.ByteMeta;
import org.dragonet.proxy.entity.meta.type.ShortMeta;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;

import cn.nukkit.entity.data.EntityMetadata;

public final class EntityMetaTranslator {

    public static EntityMetadata translateToPE(org.spacehq.mc.protocol.data.game.EntityMetadata[] pcMeta, EntityType type) {
        /*
         * Following format was fetched from http://wiki.vg/Entities#Entity_meta_Format
         */
        EntityMetadata peMeta = new EntityMetadata();// createDefault();
        for (org.spacehq.mc.protocol.data.game.EntityMetadata m : pcMeta) {
            if (m == null) {
                continue;
            }
            switch (m.getId()) {
                case 0://Flags
                    byte pcFlags = ((byte) m.getValue());
                    byte peFlags = (byte) ((pcFlags & 0x01) > 0 ? EntityMetaData.Constants.DATA_FLAG_ONFIRE : 0);
                    peFlags |= (pcFlags & 0x02) > 0 ? EntityMetaData.Constants.DATA_FLAG_SNEAKING : 0;
                    peFlags |= (pcFlags & 0x08) > 0 ? EntityMetaData.Constants.DATA_FLAG_SPRINTING : 0;
                    peFlags |= (pcFlags & 0x10) > 0 ? EntityMetaData.Constants.DATA_FLAG_ACTION : 0;
                    peFlags |= (pcFlags & 0x20) > 0 ? EntityMetaData.Constants.DATA_FLAG_INVISIBLE : 0;
                    peMeta.putByte(EntityMetaData.Constants.DATA_FLAGS, new ByteMeta(peFlags).data);
                    break;
                case 1://Air
                    peMeta.putShort(EntityMetaData.Constants.DATA_AIR, new ShortMeta((short) m.getValue()).data);
                    break;
                case 2://Name tag
                    peMeta.putString(EntityMetaData.Constants.DATA_NAMETAG, (String) m.getValue());
                    break;
                case 3://Always show name tag
                    byte data;
                    if(m.getType() == MetadataType.BYTE){
                        data = (byte) m.getValue();
                    }else if(m.getType() == MetadataType.INT){
                        data = (byte)(((int)m.getValue()) & 0xFF);
                    }else{
                        data = 1;
                    }
                    peMeta.putByte(EntityMetaData.Constants.DATA_SHOW_NAMETAG, new ByteMeta(data).data);
                    break;
                case 6://Health
                    //Not supported on MCPE yet
                    break;
                case 7://Potion color
                    peMeta.putByte(EntityMetaData.Constants.DATA_POTION_COLOR, new ByteMeta((byte) ((int) m.getValue() & 0xFF)).data);
                    break;
                case 8://Potion visible
                    peMeta.putByte(EntityMetaData.Constants.DATA_POTION_VISIBLE, new ByteMeta((byte) m.getValue()).data);
                    break;
                case 9://Arrows stick into player's body
                    //Not supported on MCPE yet
                    break;
                case 15://Has no AI
                    peMeta.putByte(EntityMetaData.Constants.DATA_NO_AI, new ByteMeta((byte) m.getValue()).data);
                    break;
                case 12://Age
                    byte age;
                    if(m.getType() == MetadataType.BYTE){
                        age = (byte) m.getValue();
                    }else if(m.getType() == MetadataType.INT){
                        age = (byte)(((int)m.getValue()) & 0xFF);
                    }else{
                        age = 0;
                    }
                    peMeta.putByte(EntityMetaData.Constants.DATA_AGE, new ByteMeta((age <= 0 ? (byte)0x1 : (byte)0x0)).data);
                    break;
                case 16:
                    //Not supported yet
                    break;
            }
        }
        return peMeta;
    }
}
