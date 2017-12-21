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
package org.dragonet.proxy.data.entity.meta.type;

import java.nio.charset.StandardCharsets;
import org.dragonet.proxy.data.entity.meta.EntityMetaData;
import org.dragonet.proxy.data.entity.meta.IEntityMetaDataObject;
import org.dragonet.proxy.utilities.BinaryStream;

public class ByteArrayMeta implements IEntityMetaDataObject {
	// vars
	public byte[] data;

	// constructor
	public ByteArrayMeta(byte[] data) {
		this.data = data;
	}

	public ByteArrayMeta(String data) {
		this(data.getBytes(StandardCharsets.UTF_8));
	}

	// public
	public int type() {
		return EntityMetaData.Constants.DATA_TYPE_STRING;
	}

	public void encode(BinaryStream out) {
		out.putByteArray(data);
	}

	// private

}
