package org.dragonet.proxy.protocol.type;

import org.dragonet.proxy.utilities.BinaryStream;

/**
 * Created on 2017/10/22.
 */
public class Skin {
	// vars
	public String skinId;
	public byte[] skinData;
	public byte[] capeData;
	public String geometryId;
	public byte[] geometryData;

	// constructor
	public Skin(String skinId, byte[] skinData, byte[] capeData, String geometryId, byte[] geometryData) {
		this.skinId = skinId;
		this.skinData = skinData;
		this.capeData = capeData;
		this.geometryId = geometryId;
		this.geometryData = geometryData;
	}

	// public
	public static Skin read(BinaryStream source) {
		return new Skin(source.getString(), source.getByteArray(), source.getByteArray(), source.getString(),
				source.getByteArray());
	}

	public void write(BinaryStream dest) {
		dest.putString(skinId);
		dest.putByteArray(skinData);
		dest.putByteArray(capeData);
		dest.putString(geometryId);
		dest.putByteArray(geometryData);
	}

	// private

}
