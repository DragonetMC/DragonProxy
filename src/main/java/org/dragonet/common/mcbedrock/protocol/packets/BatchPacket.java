package org.dragonet.common.mcbedrock.protocol.packets;

import java.util.ArrayList;
import java.util.List;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.Protocol;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;
import org.dragonet.common.mcbedrock.utilities.BinaryStream;
import org.dragonet.common.mcbedrock.utilities.Zlib;

/**
 * author: MagicDroidX
 */
public class BatchPacket extends PEPacket {

    public PEPacket[] data;

    @Override
    public int pid() {
        return ProtocolInfo.BATCH_PACKET;
    }

    @Override
    public void decodePayload() {
        byte[] data;
        try {
            data = Zlib.inflate(this.get(), 64 * 1024 * 1024);
        } catch (Exception e) {
            return;
        }

        int len = data.length;
        BinaryStream stream = new BinaryStream(data);
        try {
            List<PEPacket> packets = new ArrayList<>();
            while (stream.offset < len) {
                byte[] buf = stream.getByteArray();

                if (Protocol.packets.containsKey(buf[0])) {
                    Class<? extends PEPacket> c = Protocol.packets.get(buf[0]);
                    try {
                        PEPacket pk = c.newInstance();
                        pk.setBuffer(buf, 3); //skip 2 more bytes
                        pk.decode();
                        packets.add(pk);
                    } catch (SecurityException | InstantiationException | IllegalAccessException
                            | IllegalArgumentException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void encodePayload() {
//        not ready now
    }
}
