/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.proxy.protocol.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class BatchPacket extends PEPacket {

    public ArrayList<PEPacket> packets;

    public BatchPacket() {
        packets = new ArrayList<>();
    }

    public BatchPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.BATCH_PACKET;
    }

    @Override
    public void encode() {
        try {
            setShouldSendImmidate(true);    //We don't waste our memory

            //Combine all packets
            ByteArrayOutputStream packetCombinerData = new ByteArrayOutputStream();
            PEBinaryWriter packetCombiner = new PEBinaryWriter(packetCombinerData);
            for (PEPacket pk : packets) {
                pk.encode();
                packetCombiner.writeInt(pk.getData().length);
                packetCombiner.write(pk.getData());
            }
            Deflater def = new Deflater(7);
            def.reset();
            def.setInput(packetCombinerData.toByteArray());
            def.finish();
            byte[] deflateBuffer = new byte[65535];
            int size = def.deflate(deflateBuffer);
            deflateBuffer = Arrays.copyOfRange(deflateBuffer, 0, size);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(deflateBuffer.length);
            writer.write(deflateBuffer);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            packets = new ArrayList<>();
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            int size = reader.readInt();
            byte[] payload = reader.read(size);
            Inflater inf = new Inflater();
            inf.setInput(payload);
            byte[] decompressedPayload = new byte[1024 * 1024 * 64];
            int decompressedSize = 0;
            try {
                decompressedSize = inf.inflate(decompressedPayload);
            } catch (DataFormatException ex) {
                this.setLength(reader.totallyRead());
                return;
            }
            inf.end();

            PEBinaryReader dataReader = new PEBinaryReader(new ByteArrayInputStream(decompressedPayload));
            int offset = 0;
            while (offset < decompressedSize) {
                int pkLen = dataReader.readInt();
                offset += 4;
                byte[] pkData = dataReader.read(pkLen);
                offset += pkLen;
                PEPacket pk = Protocol.decode(pkData);
                if (pk == null) {
                    packets.clear();
                    return;
                }
                packets.add(pk);
            }
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
