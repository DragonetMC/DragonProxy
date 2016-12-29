/**
 * JRakLib is not affiliated with Jenkins Software LLC or RakNet.
 * This software is a port of RakLib https://github.com/PocketMine/RakLib.

 * This file is part of RakNet.
 *
 * JRakLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JRakLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RakNet.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.raknet.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.dragonet.raknet.protocol.Packet;
import org.dragonet.raknet.protocol.packet.ACK;
import org.dragonet.raknet.protocol.packet.ADVERTISE_SYSTEM;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_0;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_1;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_2;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_3;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_4;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_5;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_6;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_7;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_8;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_9;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_A;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_B;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_C;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_D;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_E;
import org.dragonet.raknet.protocol.packet.DATA_PACKET_F;
import org.dragonet.raknet.protocol.packet.NACK;
import org.dragonet.raknet.protocol.packet.OPEN_CONNECTION_REPLY_1;
import org.dragonet.raknet.protocol.packet.OPEN_CONNECTION_REPLY_2;
import org.dragonet.raknet.protocol.packet.OPEN_CONNECTION_REQUEST_1;
import org.dragonet.raknet.protocol.packet.OPEN_CONNECTION_REQUEST_2;
import org.dragonet.raknet.protocol.packet.UNCONNECTED_PING;
import org.dragonet.raknet.protocol.packet.UNCONNECTED_PING_OPEN_CONNECTIONS;
import org.dragonet.raknet.protocol.packet.UNCONNECTED_PONG;

/**
 * Manager for handling a connection. The manager handles the internal ticking, and networking.
 *
 * @author jython234
 */
public class ConnectionManager {
    protected Map<Byte, Class<? extends Packet>> packetPool = new ConcurrentHashMap<>();

    protected JRakLibClient client;

    protected UDPClientSocket socket;

    protected int receiveBytes = 0;
    protected int sendBytes = 0;

    protected boolean shutdown = false;

    protected int ticks = 0;
    protected long lastMeasure;

    protected Connection connection;

    public boolean portChecking = false;
    
    public ConnectionManager(JRakLibClient client, UDPClientSocket socket){
        this.client = client;
        this.socket = socket;
        registerPackets();

        try {
            if(!connect(1447, 4)){
                //getLogger().notice("Failed to connect to "+client.getServerEndpoint()+": no response.");
                client.pushMainToThreadPacket(new byte[] {RakNet.PACKET_SHUTDOWN});
            } else {
                run();
            }
        } catch (IOException e) {
            //getLogger().emergency("*** FAILED TO CONNECT TO " + client.getServerEndpoint() + ": IOException: " + e.getMessage());
            e.printStackTrace();
            client.pushMainToThreadPacket(new byte[]{RakNet.PACKET_EMERGENCY_SHUTDOWN});
        }
    }

    private boolean connect(int payloadSize, int packets) throws IOException {
        System.out.println("[DEBUG] Client connecting, PLOAD=" + payloadSize + ", TRIES=" + packets);
        for(int i = 0; i < packets; i++){
            System.out.println("[DEBUG] Trying to connect, TRY=" + i + "... ");
            OPEN_CONNECTION_REQUEST_1 request1 = new OPEN_CONNECTION_REQUEST_1();
            request1.protocol = RakNet.PROTOCOL;
            request1.mtuSize = (short) payloadSize;
            request1.encode();
            socket.writePacket(request1.buffer, client.getServerEndpoint());

            DatagramPacket response = socket.readPacketBlocking(500);
            if(response != null && response.getData()[0] == OPEN_CONNECTION_REPLY_1.ID){
                connection = new Connection(this, payloadSize);
                Packet packet = getPacketFromPool(response.getData()[0]);
                packet.buffer = response.getData();
                connection.handlePacket(packet);
                return true;
            }
        }
        if(payloadSize == 1447){
            return connect(1155, 4);
        } else if(payloadSize == 1155){
            return connect(531, 5);
        } else {
            return false;
        }
    }

    public void run(){
        try {
            tickProcessor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tickProcessor() throws IOException {
        lastMeasure = Instant.now().toEpochMilli();

        while(!shutdown){
            long start = Instant.now().toEpochMilli();
            int max = 5000;
            while(receivePacket()){
                max = max - 1;
            }
            while(receiveStream());
            long time = Instant.now().toEpochMilli() - start;
            if(time < 50){ //20 ticks per second (1000 / 20)
                sleepUntil(Instant.now().toEpochMilli()+(50 - time));
            }
            tick();
        }
    }

    private void tick() throws IOException {
        long time = Instant.now().toEpochMilli();
        connection.update(time);

        if((ticks & 0b1111) == 0){
            double diff = Math.max(0.005d, time - lastMeasure);
            streamOption("bandwith", "up:"+(sendBytes / diff)+",down:"+(receiveBytes / diff)); //TODO: Fix this stuff
            lastMeasure = time;
            sendBytes = 0;
            receiveBytes = 0;
        }
        ticks = ticks + 1;
    }

    private boolean receivePacket() throws IOException {
        DatagramPacket packet = socket.readPacket();
        if(packet == null) {
            return false;
        }
        int len = packet.getLength();
        if(len > 0){
            SocketAddress source = packet.getSocketAddress();
            receiveBytes += len;

            Packet pkt = getPacketFromPool(packet.getData()[0]);
            if(pkt != null){
                pkt.buffer = packet.getData();
                connection.handlePacket(pkt);
                return true;
            } else if (packet.getData() != null){
                streamRaw(source, packet.getData());
                return true;
            } else {
                //getLogger().notice("Dropped packet: "+ Arrays.toString(packet.getData()));
                return false;
            }
        }
        return false;
    }

    public void streamEncapsulated(EncapsulatedPacket packet){
        streamEncapsulated(packet, RakNet.PRIORITY_NORMAL);
    }

    public void streamEncapsulated(EncapsulatedPacket packet, byte flags){
        String id = client.getServerIP() + ":" + client.getServerPort();
        ByteBuffer bb = ByteBuffer.allocate(packet.getTotalLength());
        bb.put(RakNet.PACKET_ENCAPSULATED).put((byte) id.getBytes().length).put(id.getBytes()).put(flags).put(packet.toBinary(true));
        client.pushThreadToMainPacket(bb.array());
    }

    public void streamRaw(SocketAddress address, byte[] payload){
        String dest;
        int port;
        if(address.toString().contains("/")) {
            dest = address.toString().split(Pattern.quote("/"))[1].split(Pattern.quote(":"))[0];
            port = Integer.parseInt(address.toString().split(Pattern.quote("/"))[1].split(Pattern.quote(":"))[1]);
        } else {
            dest = address.toString().split(Pattern.quote(":"))[0];
            port = Integer.parseInt(address.toString().split(Pattern.quote(":"))[1]);
        }
        streamRaw(dest, port, payload);
    }

    public void streamRaw(String address, int port, byte[] payload){
        ByteBuffer bb = ByteBuffer.allocate(4 + address.getBytes().length + payload.length);
        bb.put(RakNet.PACKET_RAW).put((byte) address.getBytes().length).put(address.getBytes()).put(Binary.writeShort((short) port)).put(payload);
        client.pushThreadToMainPacket(bb.array());
    }

    protected void streamClose(String identifier, String reason){
        ByteBuffer bb = ByteBuffer.allocate(3 + identifier.getBytes().length + reason.getBytes().length);
        bb.put(RakNet.PACKET_CLOSE_SESSION).put((byte) identifier.getBytes().length).put(identifier.getBytes()).put((byte) reason.getBytes().length).put(reason.getBytes());
        client.pushThreadToMainPacket(bb.array());
    }

    protected void streamInvalid(String identifier){
        ByteBuffer bb = ByteBuffer.allocate(2+identifier.getBytes().length);
        bb.put(RakNet.PACKET_INVALID_SESSION).put((byte) identifier.getBytes().length).put(identifier.getBytes());
        client.pushThreadToMainPacket(bb.array());
    }

    protected void streamOpen(long serverId){
        String identifier = client.getServerIP() + ":" + client.getServerPort();
        ByteBuffer bb = ByteBuffer.allocate(10 + identifier.getBytes().length);
        bb.put(RakNet.PACKET_OPEN_SESSION).put((byte) identifier.getBytes().length).put(identifier.getBytes()).put(Binary.writeLong(serverId));
        client.pushThreadToMainPacket(bb.array());
    }

    protected void streamACK(String identifier, int identifierACK){
        ByteBuffer bb = ByteBuffer.allocate(6+identifier.getBytes().length);
        bb.put(RakNet.PACKET_ACK_NOTIFICATION).put((byte) identifier.getBytes().length).put(identifier.getBytes()).put(Binary.writeInt(identifierACK));
        client.pushThreadToMainPacket(bb.array());
    }

    protected void streamOption(String name, String value){
        ByteBuffer bb = ByteBuffer.allocate(2+name.getBytes().length+value.getBytes().length);
        bb.put(RakNet.PACKET_SET_OPTION).put((byte) name.getBytes().length).put(name.getBytes()).put(value.getBytes());
        client.pushThreadToMainPacket(bb.array());
    }

    public void sendPacket(Packet packet, String dest, int port) throws IOException {
        packet.encode();
        sendBytes += packet.buffer.length;
        socket.writePacket(packet.buffer, new InetSocketAddress(dest, port));
    }

    public boolean receiveStream() throws IOException {
        byte[] packet = client.readMainToThreadPacket();
        if(packet == null){
            return false;
        }
        if(packet.length > 0){
            byte id = packet[0];
            int offset = 1;
            if(id == RakNet.PACKET_ENCAPSULATED){
                int len = packet[offset++];
                String identifier = new String(Binary.subbytes(packet, offset, len));
                offset += len;
                byte flags = packet[offset++];
                byte[] buffer = Binary.subbytes(packet, offset);
                connection.addEncapsulatedToQueue(EncapsulatedPacket.fromBinary(buffer, true), flags);
            } else if(id == RakNet.PACKET_RAW){
                int len = packet[offset++];
                String address = new String(Binary.subbytes(packet, offset, len));
                offset += len;
                int port = Binary.readShort(Binary.subbytes(packet, offset, 2));
                offset += 2;
                byte[] payload = Binary.subbytes(packet, offset);
                socket.writePacket(payload, new InetSocketAddress(address, port));
            } else if(id == RakNet.PACKET_CLOSE_SESSION){
                /*
                int len = packet[offset++];
                String identifier = new String(Binary.subbytes(packet, offset, len));
                */
                client.pushThreadToMainPacket(packet);
            } else if(id == RakNet.PACKET_SET_OPTION){
                int len = packet[offset++];
                String name = new String(Binary.subbytes(packet, offset, len));
                offset += len;
                String value = new String(Binary.subbytes(packet, offset));
                switch(name){
                    case "portChecking":
                        portChecking = Boolean.parseBoolean(value);
                        break;
                }
            } else if(id == RakNet.PACKET_SHUTDOWN){
                connection.onShutdown();

                socket.close();
                shutdown = true;
            } else if(id == RakNet.PACKET_EMERGENCY_SHUTDOWN){
                shutdown = true;
            } else {
                return false;
            }
            return true;
        }
        return false;
    }
    
    private void registerPacket(byte id, Class<? extends Packet> clazz){
        packetPool.put(id, clazz);
    }
    
    public Packet getPacketFromPool(byte id){
        if(packetPool.containsKey(id)){
            try {
                return packetPool.get(id).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void registerPackets() {
        registerPacket(UNCONNECTED_PING.ID, UNCONNECTED_PING.class);
        registerPacket(UNCONNECTED_PING_OPEN_CONNECTIONS.ID, UNCONNECTED_PING_OPEN_CONNECTIONS.class);
        registerPacket(OPEN_CONNECTION_REQUEST_1.ID, OPEN_CONNECTION_REQUEST_1.class);
        registerPacket(OPEN_CONNECTION_REPLY_1.ID, OPEN_CONNECTION_REPLY_1.class);
        registerPacket(OPEN_CONNECTION_REQUEST_2.ID, OPEN_CONNECTION_REQUEST_2.class);
        registerPacket(OPEN_CONNECTION_REPLY_2.ID, OPEN_CONNECTION_REPLY_2.class);
        registerPacket(UNCONNECTED_PONG.ID, UNCONNECTED_PONG.class);
        registerPacket(ADVERTISE_SYSTEM.ID, ADVERTISE_SYSTEM.class);
        registerPacket(DATA_PACKET_0.ID, DATA_PACKET_0.class);
        registerPacket(DATA_PACKET_1.ID, DATA_PACKET_1.class);
        registerPacket(DATA_PACKET_2.ID, DATA_PACKET_2.class);
        registerPacket(DATA_PACKET_3.ID, DATA_PACKET_3.class);
        registerPacket(DATA_PACKET_4.ID, DATA_PACKET_4.class);
        registerPacket(DATA_PACKET_5.ID, DATA_PACKET_5.class);
        registerPacket(DATA_PACKET_6.ID, DATA_PACKET_6.class);
        registerPacket(DATA_PACKET_7.ID, DATA_PACKET_7.class);
        registerPacket(DATA_PACKET_8.ID, DATA_PACKET_8.class);
        registerPacket(DATA_PACKET_9.ID, DATA_PACKET_9.class);
        registerPacket(DATA_PACKET_A.ID, DATA_PACKET_A.class);
        registerPacket(DATA_PACKET_B.ID, DATA_PACKET_B.class);
        registerPacket(DATA_PACKET_C.ID, DATA_PACKET_C.class);
        registerPacket(DATA_PACKET_D.ID, DATA_PACKET_D.class);
        registerPacket(DATA_PACKET_E.ID, DATA_PACKET_E.class);
        registerPacket(DATA_PACKET_F.ID, DATA_PACKET_F.class);
        registerPacket(NACK.ID, NACK.class);
        registerPacket(ACK.ID, ACK.class);
    }

    public JRakLibClient getClient() {
        return client;
    }

    public UDPClientSocket getSocket() {
        return socket;
    }
    
    public static void sleepUntil(long time) {
        while (true) {
            if (Instant.now().toEpochMilli() >= time) {
                break;
            }
        }
    }
}
