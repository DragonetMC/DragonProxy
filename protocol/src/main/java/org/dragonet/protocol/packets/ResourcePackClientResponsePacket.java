package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

import java.util.UUID;

/**
 * Created on 2017/10/22.
 */
public class ResourcePackClientResponsePacket extends PEPacket {

    public static final byte STATUS_REFUSED = 1;
    public static final byte STATUS_SEND_PACKS = 2;
    public static final byte STATUS_HAVE_ALL_PACKS = 3;
    public static final byte STATUS_COMPLETED = 4;

    public byte status;
    public Entry[] packEntries = new Entry[0];

    public ResourcePackClientResponsePacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;
    }

    @Override
    public void encodePayload() {
        putByte(status);
        putLShort(packEntries.length);
        for (Entry entry : packEntries) {
            putString(entry.uuid.toString() + '_' + entry.version);
        }
    }

    @Override
    public void decodePayload() {
        status = (byte) (getByte() & 0xFF);
        int len = getLShort();
        if (len > 0) {
            packEntries = new Entry[len];
            for (int i = 0; i < len; i++) {
                String[] uuidVersion = getString().split("_");
                packEntries[i] = new Entry(UUID.fromString(uuidVersion[0]), uuidVersion[1]);
            }
        }
    }

    public static class Entry {
        public final UUID uuid;
        public final String version;

        public Entry(UUID uuid, String version) {
            this.uuid = uuid;
            this.version = version;
        }
    }
}
