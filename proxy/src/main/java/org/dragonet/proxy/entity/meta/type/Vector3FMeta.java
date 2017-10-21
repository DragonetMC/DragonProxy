package org.dragonet.proxy.entity.meta.type;

import org.dragonet.proxy.entity.meta.EntityMetaData;
import org.dragonet.proxy.entity.meta.EntityMetaDataObject;
import org.dragonet.proxy.utilities.BinaryStream;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class Vector3FMeta implements EntityMetaDataObject {

    public Vector3F vector;

    public Vector3FMeta(Vector3F vector) {
        this.vector = vector;
    }

    @Override
    public int type() {
        return EntityMetaData.Constants.DATA_TYPE_VECTOR3F;
    }

    @Override
    public void encode(BinaryStream out) {
        out.putVector3F(vector);
    }
}
