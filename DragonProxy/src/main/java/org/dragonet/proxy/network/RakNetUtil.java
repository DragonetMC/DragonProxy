/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network;

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.utils.BinaryStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.protocol.message.CustomPacket;
import net.marfgamer.jraknet.protocol.message.EncapsulatedPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.utilities.Zlib;
import sul.protocol.pocket101.play.Batch;
import sul.utils.Buffer;
import sul.utils.Packet;

/**
 *
 * @author enims
 */
public class RakNetUtil {

    public static Batch batchPackets(sul.utils.Packet... packets) {
        BinaryStream str = new BinaryStream();
        for (sul.utils.Packet packet : packets) {
            byte[] payload = packet.encode();
            str.putUnsignedVarInt(payload.length);
            str.putByteArray(payload);
        }

        byte[] data = new byte[0];
        try {
            data = Zlib.deflate(str.getBuffer(), Deflater.BEST_COMPRESSION); // TODO: Replace the 0 with the configuration for network compression level
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Batch batch = new Batch(str.get()) {

            @Override
            public byte[] encode() {
                this._buffer = new byte[this.length()];
                this.writeBigEndianByte(ID);

                Buffer buffer = new Buffer();
                buffer._buffer = new byte[4 + data.length]; // The 4 is the length in bytes of an Unsigned Varint
                buffer.writeVarint(data.length);
                buffer.writeBytes(data);

                try {
                    data = Zlib.deflate(buffer.getBuffer()); // TODO: Replace the 256 with the configuration for network compression level
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.writeBytes(data);
                return this.getBuffer();
            }

            @Override
            public void decode(byte[] buffer) {
                this._buffer = buffer;
                readBigEndianByte();

                try {
                    Buffer buf = new Buffer();
                    buf._buffer = Zlib.inflate(this.readBytes(_buffer.length - _index), 64 * 1024 * 1024);
                    buf.readVaruint();
                    this.data = getBuffer();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }

        };
        return batch;*/
        return new Batch(data); // This is the way the packet's used to be prepared. Still Works
    }

    public static sul.utils.Packet[] decodeBatch(Batch batch) {
        List<sul.utils.Packet> packets = new ArrayList<>();

        try {
            byte[] buffer = Zlib.inflate(batch.data);
            int bufferIndex = 0;

            while (bufferIndex < buffer.length) {
                sul.utils.Packet pk = getPacket(Arrays.copyOfRange(buffer, bufferIndex, buffer.length));
                bufferIndex += pk.length();
                packets.add(pk);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return packets.toArray(new Packet[0]);
    }

    public static sul.utils.Packet getPacket(byte id) {
        Class<? extends Packet> clazz = sul.protocol.pocket101.Packets.PLAY.get((int) id);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(RakNetUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("No packet with id " + id + " found");
        Thread.dumpStack();
        return null;
    }

    public static sul.utils.Packet getPacket(byte[] data) {
        sul.utils.Packet packet = getPacket(data[0]);
        if (packet != null) {
            try {
                packet.decode(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return packet;
    }

    public static sul.utils.Packet getPacket(RakNetPacket pack) {
        return getPacket(Arrays.copyOfRange(pack.buffer().array(), 1, pack.buffer().array().length));
    }

    public static RakNetPacket prepareToSend(byte[] buffer) {
        /*if (!(packet instanceof Batch) && packet.length() > 512) {
            pk = batchPackets(packet);
        }*/

        return new RakNetPacket(append(buffer));
    }

    public static RakNetPacket prepareToSend(sul.utils.Packet packet) {
        sul.utils.Packet pk = packet;
        if (!(packet instanceof Batch) && packet.length() > 512) {
            pk = batchPackets(packet);
        }

        return new RakNetPacket(append(pk.encode())); // The packet encapsulation is handled by JRakNet
    }

    private static byte[] append(byte[] buff) {
        byte[] buff2 = new byte[buff.length + 1];
        int index = 0;
        buff2[index++] = (byte) 0xFE;
        for (byte b : buff) {
            buff2[index++] = b;
        }
        return buff2;
    }

    public static void handlePackets(ClientConnection session, RakNetPacket raknetPk, sul.utils.Packet packet, boolean useUpstream) {
        if (session.isPassthrough()) {
            if (useUpstream) {
                session.getUpstreamProtocol().sendPacket(raknetPk, session);
            } else {
                session.getDownstreamProtocol().sendPacket(raknetPk);
            }
        } else {
            Object[] packets = {packet};
            if (packet instanceof Batch) {
                sul.utils.Packet[] pkts = RakNetUtil.decodeBatch((Batch) packet);
                packets = pkts;
            }

            for (Object pack : packets) {
                Object[] packetList = PacketTranslatorRegister.translateToPC(session, (Packet) pack);
                for (Object obj : packetList) {
                    if (useUpstream) {
                        session.getUpstreamProtocol().sendPacket(obj, session);
                    } else {
                        session.getDownstreamProtocol().sendPacket(obj);
                    }
                }
            }
        }
    }
}
