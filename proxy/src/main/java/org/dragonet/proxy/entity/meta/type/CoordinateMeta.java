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

public class CoordinateMeta implements EntityMetaDataObject {

    public int data1;
    public int data2;
    public int data3;

    public CoordinateMeta(int data1, int data2, int data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    @Override
    public int type() {
        return EntityMetaData.Constants.DATA_TYPE_POS;
    }

    @Override
    public byte[] encode() {
        ByteBuffer buff = ByteBuffer.allocate(12);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putInt(this.data1);
        buff.putInt(this.data2);
        buff.putInt(this.data3);
        return buff.array();
    }

}
