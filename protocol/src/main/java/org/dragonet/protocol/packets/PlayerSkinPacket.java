package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.data.entity.Skin;

import java.util.UUID;

/**
 * Created on 2017/11/26.
 */
public class PlayerSkinPacket extends PEPacket {

    public UUID uuid;
    public String oldSkinName = "";
    public String newSkinName = "";
    public Skin skin;
    public String geometryModel;
    public String geometryData;

    public PlayerSkinPacket() {
    }

    public PlayerSkinPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_SKIN_PACKET;
    }

    @Override
    public void encodePayload() {
        putUUID(uuid);
        if (skin == null) {
            skin = Skin.DEFAULT_SKIN_STEVE;
        }
        putString(skin.getModel());
        putString(oldSkinName);
        putString(newSkinName);
        putByteArray(skin.getData());
        putByteArray(skin.getCape().getData());
        putString(geometryModel);
        putString(geometryData);

    }

    @Override
    public void decodePayload() {

    }
}
