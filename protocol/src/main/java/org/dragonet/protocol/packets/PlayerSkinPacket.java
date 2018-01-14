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
            skin = Skin.DEFAULT_SKIN;
        }
        putString(skin.skinId);
        putString(oldSkinName);
        putString(newSkinName);
        putByteArray(skin.skinData);
        putByteArray(skin.capeData);
        putString(skin.geometryId);
        putByteArray(skin.geometryData);

    }

    @Override
    public void decodePayload() {

    }
}
