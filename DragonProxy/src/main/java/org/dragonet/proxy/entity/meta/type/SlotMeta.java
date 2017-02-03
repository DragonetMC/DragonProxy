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
package org.dragonet.proxy.entity.meta.type;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.entity.meta.EntityMetaDataObject;

public class SlotMeta implements EntityMetaDataObject {

    public short data1;
    public byte data2;
    public short data3;

    public SlotMeta(short data1, byte data2, short data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    @Override
    public int type() {
        return EntityMetaData.Constants.DATA_TYPE_SLOT;
    }

    @Override
    public byte[] encode() {
        ByteBuffer buff = ByteBuffer.allocate(5);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putShort(this.data1);
        buff.put(this.data2);
        buff.putShort(this.data3);
        return buff.array();
    }

}
