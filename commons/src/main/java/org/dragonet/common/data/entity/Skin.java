package org.dragonet.common.data.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.dragonet.common.utilities.BinaryStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created on 2017/10/22.
 */
public class Skin {

    public final static Skin DEFAULT_SKIN;

    public final String skinId;
    public String skinData;
    public String capeData;
    public String geometryId;
    public String geometryData;

    static {
        byte[] load = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream ins = Skin.class.getResourceAsStream("/default-skin.json");
            byte[] buffer = new byte[2048];
            int read;
            while ((read = ins.read(buffer)) != -1)
                bos.write(buffer, 0, read);
            bos.close();
            load = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String rawSkin = new String(load, Charset.forName("UTF-8"));

            if (load != null)
                DEFAULT_SKIN = Skin.read(rawSkin);
            else
                DEFAULT_SKIN = null;
        }
    }

    public Skin(String skinId, String skinData, String capeData, String geometryId, String geometryData) {
        this.skinId = skinId;
        this.skinData = skinData;
        this.capeData = capeData;
        this.geometryId = geometryId;
        this.geometryData = geometryData;
    }

    public static Skin read(JsonObject json) {
        if (json.has("SkinId") && json.has("SkinData") && json.has("CapeData") && json.has("SkinGeometryName") && json.has("SkinGeometry")) {
            Skin skin = new Skin(
                    json.get("SkinId").getAsString(),
                    json.get("SkinData").getAsString(),
                    json.get("CapeData").getAsString(),
                    json.get("SkinGeometryName").getAsString(),
                    json.get("SkinGeometry").getAsString());
            return skin;
        }
        return null;
    }

    public static Skin read(String rawSkin) {
        return read(new Gson().fromJson(rawSkin, JsonObject.class));
    }

    public static Skin read(BinaryStream source) {
        return new Skin(source.getString(), source.getString(), source.getString(), source.getString(), source.getString());
    }

    public void write(BinaryStream dest) {
        dest.putString(skinId);
        dest.putString(skinData);
        dest.putString(capeData);
        dest.putString(geometryId);
        dest.putString(geometryData);
    }
}
