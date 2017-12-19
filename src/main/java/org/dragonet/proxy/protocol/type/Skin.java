package org.dragonet.proxy.protocol.type;

import org.dragonet.proxy.utilities.BinaryStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created on 2017/10/22.
 */
public class Skin {

	public final static byte[] DEFAULT_SKIN_FULL_BINARY;
	public final static Skin DEFAULT_SKIN;

	static {
		byte[] load = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InputStream ins = Skin.class.getResourceAsStream("/STEVE_SKIN_FULL_BINARY.bin");
			byte[] buffer = new byte[2048];
			int read;
			while ((read = ins.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
			bos.close();
			load = bos.toByteArray();
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			DEFAULT_SKIN_FULL_BINARY = load;

			if(load != null) {
				DEFAULT_SKIN = Skin.read(new BinaryStream(load));
			} else {
				DEFAULT_SKIN = null;
			}
		}
	}

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
