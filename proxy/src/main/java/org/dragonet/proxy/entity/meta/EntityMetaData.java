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
package org.dragonet.proxy.entity.meta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.dragonet.proxy.entity.meta.type.ByteArrayMeta;
import org.dragonet.proxy.entity.meta.type.ByteMeta;
import org.dragonet.proxy.entity.meta.type.ShortMeta;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class EntityMetaData {

    public static class Constants {

        public final static int DATA_TYPE_BYTE = 0;
        public final static int DATA_TYPE_SHORT = 1;
        public final static int DATA_TYPE_INT = 2;
        public final static int DATA_TYPE_FLOAT = 3;
        public final static int DATA_TYPE_STRING = 4;
        public final static int DATA_TYPE_SLOT = 5;
        public final static int DATA_TYPE_POS = 6;
        public final static int DATA_TYPE_ROTATION = 7;
        public final static int DATA_TYPE_LONG = 8;

        public final static int DATA_FLAGS = 0;
        public final static int DATA_AIR = 1;
        public final static int DATA_NAMETAG = 2;
        public final static int DATA_SHOW_NAMETAG = 3;
        public final static int DATA_SILENT = 4;
        public final static int DATA_POTION_COLOR = 7;
        public final static int DATA_POTION_VISIBLE = 8;
        public final static int DATA_AGE = 14; //Not sure
        public final static int DATA_NO_AI = 15;

        public final static int DATA_FLAG_ONFIRE = 0;
        public final static int DATA_FLAG_SNEAKING = 1 << 1;
        public final static int DATA_FLAG_RIDING = 1 << 2;
        public final static int DATA_FLAG_SPRINTING = 1 << 3;
        public final static int DATA_FLAG_ACTION = 1 << 4;
        public final static int DATA_FLAG_INVISIBLE = 1 << 5;
    }

    public HashMap<Integer, EntityMetaDataObject> map;

    public EntityMetaData() {
        this.map = new HashMap<>();
    }

    public void set(int key, EntityMetaDataObject object) {
        this.map.put(key, object);
    }

    public byte[] encode() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PEBinaryWriter writer = new PEBinaryWriter(bos);
        try {
            for (Map.Entry<Integer, EntityMetaDataObject> entry : this.map.entrySet()) {
                writer.writeByte(((byte) (((entry.getValue().type() & 0xFF) << 5) | (entry.getKey() & 0x1F))));
                writer.write(entry.getValue().encode());
            }
            writer.writeByte((byte) 0x7F);
            writer.close();
            return bos.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    public static EntityMetaData createDefault() {
        EntityMetaData data = new EntityMetaData();
        data.set(EntityMetaData.Constants.DATA_FLAGS, new ByteMeta((byte) 0));
        data.set(EntityMetaData.Constants.DATA_AIR, new ShortMeta((short) 300));
        data.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(""));
        data.set(EntityMetaData.Constants.DATA_SHOW_NAMETAG, new ByteMeta((byte) 0x01));
        data.set(EntityMetaData.Constants.DATA_SILENT, new ByteMeta((byte) 0));
        data.set(EntityMetaData.Constants.DATA_NO_AI, new ByteMeta((byte) 0));
        return data;
    }

    /*
    public static EntityMetaData getMetaDataFromPlayer(GlowPlayer player) {
        byte flags = (byte) 0x00;
        if (player.getFireTicks() > 0) {
            flags |= EntityMetaData.Constants.DATA_FLAG_ONFIRE;
        }
        if(player.isSprinting()){
            flags |= EntityMetaData.Constants.DATA_FLAG_SPRINTING;
        }
        if(player.isSneaking()){
            flags |= EntityMetaData.Constants.DATA_FLAG_SNEAKING;
        }
        EntityMetaData data = createDefault();
        data.set(EntityMetaData.Constants.DATA_FLAGS, new ByteMeta(flags));
        data.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(player.getDisplayName()));
        return data;
    }
     */
}
