package org.dragonet.proxy.data.entity.meta.type;

import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.data.entity.meta.IEntityMetaDataObject;
import org.dragonet.proxy.utilities.BinaryStream;
import org.dragonet.proxy.utilities.Vector3F;

/**
 * Created on 2017/10/21.
 */
public class Vector3FMeta implements IEntityMetaDataObject {
	// vars
	public Vector3F vector;

	// constructor
	public Vector3FMeta(Vector3F vector) {
		this.vector = vector;
	}

	// public
	public int type() {
		return EntityMetaData.Constants.DATA_TYPE_VECTOR3F;
	}

	public void encode(BinaryStream out) {
		out.putVector3F(vector);
	}

	// private

}
