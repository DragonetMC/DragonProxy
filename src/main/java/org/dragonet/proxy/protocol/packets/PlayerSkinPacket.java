package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.protocol.type.Skin;

import java.util.UUID;

/**
 * Created on 2017/11/26.
 */
public class PlayerSkinPacket extends PEPacket {

    public UUID uuid;
    public String oldSkinName = "";
    public String newSkinName = "";
    public Skin skin;

    // constructor
    public PlayerSkinPacket() {
    }

    public PlayerSkinPacket(UUID uuid) {
        this.uuid = uuid;
    }

    // public
    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_SKIN_PACKET;
    }

    @Override
    public void encodePayload() {
        putUUID(uuid);
        if (skin == null) skin = Skin.DEFAULT_SKIN;
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

    // private
}
