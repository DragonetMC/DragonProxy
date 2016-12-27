/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import lombok.Getter;

import org.dragonet.proxy.protocol.Protocol;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Zlib;

public class PEPacketProcessor implements Runnable {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    @Getter
    private final UpstreamSession client;

    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(UpstreamSession client) {
        this.client = client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void run() {
        int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] bin = packets.pop();
            DataPacket packet = Protocol.decode(bin);
            if (packet == null) {
                continue;
            }
            handlePacket(packet);
        }
    }

    public void handlePacket(DataPacket packet) {
        if (packet == null) {
            return;
        }

        if (BatchPacket.class.isAssignableFrom(packet.getClass())) {
            //((BatchPacket) packet).packets.stream().filter((pk) -> !(pk == null)).forEach((pk) -> {
            //    handlePacket(pk);
            //});
        	System.err.println("Batch packet 2");
        	BatchPacket pack = (BatchPacket) packet;
        	
        	//pack.setBuffer(pack.payload, 1);
        	pack.decode();
        	
        	for(DataPacket pk : processBatch(pack)){
                try {
                	handlePacket(pk);
                } catch (Exception e){
                	e.printStackTrace();
                }
        	}

            return;
        }
        
        switch (packet.pid()) {
            case ProtocolInfo.LOGIN_PACKET:
                client.onLogin((LoginPacket) packet);
                break;
            case ProtocolInfo.TEXT_PACKET:  //Login
                if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null) {
                    PacketTranslatorRegister.translateToPC(client, packet);
                    break;
                }
            default:
                if (client.getDownstream() == null) {
                    break;
                }
                if (!client.getDownstream().isConnected()) {
                    break;
                }
                Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
                if (translated == null || translated.length == 0) {
                    break;
                }
                client.getDownstream().send(translated);
                break;
        }
    }
    
    private List<DataPacket> processBatch(BatchPacket packet) {
        byte[] data;
        try {
            data = Zlib.inflate(packet.payload, 64 * 1024 * 1024);
        } catch (Exception e) {
        	e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        int len = data.length;
        BinaryStream stream = new BinaryStream(data);
        try {
            List<DataPacket> packets = new ArrayList<>();
            while (stream.offset < len) {
                byte[] buf = stream.getByteArray();

                DataPacket pk;
                if ((pk = Protocol.decode(buf)) != null) {
                    if (pk.pid() == ProtocolInfo.BATCH_PACKET) {
                        throw new IllegalStateException("Invalid BatchPacket inside BatchPacket");
                    }
                    packets.add(pk);
                }
            }
            return packets;

        } catch (Exception e) {
            System.err.println("BatchPacket 0x" + Binary.bytesToHexString(packet.payload));
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

}
