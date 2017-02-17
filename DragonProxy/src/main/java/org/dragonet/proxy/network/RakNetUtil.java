/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.network;

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.utils.BinaryStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.protocol.Reliability;
import net.marfgamer.jraknet.protocol.message.CustomPacket;
import net.marfgamer.jraknet.protocol.message.EncapsulatedPacket;
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
            str.putByteArray(packet.encode());
        }

        // Nasty hack to work around problems with the existing encode and decode methods
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
        return new Batch(str.get()); // This is the way the packet's used to be prepared. Still Works
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
            packet.decode(data);
        }
        return packet;
    }

    public static sul.utils.Packet getPacket(RakNetPacket pack) {
        return getPacket(pack.buffer().array());
    }

    public static RakNetPacket prepareToSend(sul.utils.Packet packet, Reliability reliability) {
        sul.utils.Packet pk = packet;
        if(!(packet instanceof Batch) && packet.length() > 512){
            pk = batchPackets(packet);
        }
        
        /*EncapsulatedPacket enc = new EncapsulatedPacket();
        enc.reliability = reliability;
        enc.payload = new RakNetPacket(append(pk.encode()));
        enc.encode();
        return new RakNetPacket(enc.buffer.array());*/
        return new RakNetPacket(append(pk.encode())); // This is the way the packet's used to be prepared. Still Works
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
}
