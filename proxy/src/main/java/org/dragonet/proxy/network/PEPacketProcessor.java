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

import java.io.FileOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import com.github.steveice10.packetlib.packet.Packet;
import lombok.Getter;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.protocol.packets.LoginPacket;

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
            byte[] p = packets.pop();
            PEPacket[] packets;
            try {
                packets = Protocol.decode(p);
                if (packets == null && packets.length > 0) {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            for (PEPacket decoded : packets) {
                handlePacket(decoded);
            }
        }

    }

    public void handlePacket(PEPacket packet) {
        if (packet == null) {
            return;
        }

        System.out.println("RECEIVED PACKET=" + packet.getClass().getSimpleName());
        try{
            FileOutputStream fos = new FileOutputStream("cap_" + System.currentTimeMillis() + "_" + packet.getClass().getSimpleName() + ".bin");
            fos.write(packet.getBuffer());
            fos.close();
        }catch(Exception e){}

        switch (packet.pid()) {
            case 1:
                client.onLogin((LoginPacket) packet);
                break;
            case 9:  //Text (check CLS Login)
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

}
