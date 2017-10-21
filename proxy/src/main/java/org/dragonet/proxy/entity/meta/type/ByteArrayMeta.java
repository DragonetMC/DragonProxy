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

import java.nio.charset.StandardCharsets;
import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.entity.meta.EntityMetaDataObject;
import org.dragonet.proxy.utilities.BinaryStream;

public class ByteArrayMeta implements EntityMetaDataObject {

    public byte[] data;

    public ByteArrayMeta(byte[] data) {
        this.data = data;
    }

    public ByteArrayMeta(String data) {
        this(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int type() {
        return EntityMetaData.Constants.DATA_TYPE_STRING;
    }

    @Override
    public void encode(BinaryStream out) {
        out.putByteArray(data);
    }
}
