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
import java.util.Deque;

import lombok.Getter;

public class PEPacketProcessor implements Runnable {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    @Getter
    private final ClientConnection client;

    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(ClientConnection client) {
        this.client = client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void run() {
       /* int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] bin = packets.pop();
            DataPacket[] packets = RakNetProtocol.handleRaw(bin);
            if (packets == null || packets.length < 1) {
                continue;
            }

            for (DataPacket packet : packets) {
                handlePacket(packet);
            }
        }*/
    }

    @SuppressWarnings("unchecked")
    public void handlePacket(DataPacket packet) {
        if (packet != null) {
            if (client.getStatus() == ConnectionStatus.CONNECTED) {
                //Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
               // if (translated != null && translated.length > 0 && client.getDownstream() != null && client.getDownstream().isConnected()) {
                    //client.getDownstream().send(translated);
               // }
            } else {
                System.err.println("Ignoring packet sent to unconnected session");
            }
        }
    }
}
