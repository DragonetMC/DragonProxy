package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.BlockPosition;

/**
 * Created on 2017/10/21.
 */
public class PlaySoundPacket extends PEPacket {

    public String name;
    public BlockPosition blockPosition;
    public float volume;
    public float pitch;

    @Override
    public int pid() {
        return ProtocolInfo.PLAY_SOUND_PACKET;
    }

    @Override
    public void encodePayload() {
        putString(name);
        putBlockPosition(new BlockPosition(blockPosition.x * 8, blockPosition.y * 8, blockPosition.z * 8));
        putLFloat(volume);
        putLFloat(pitch);
    }

    @Override
    public void decodePayload() {
        name = getString();
        blockPosition = getBlockPosition();
        blockPosition.x /= 8;
        blockPosition.y /= 8;
        blockPosition.z /= 8;
        volume = getLFloat();
        pitch = getLFloat();
    }
}
