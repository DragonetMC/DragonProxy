/**
 * JRakLib is not affiliated with Jenkins Software LLC or RakNet.
 * This software is a port of RakLib https://github.com/PocketMine/RakLib.

 * This file is part of JRakLib.
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
 * along with JRakLib.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.raknet.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.dragonet.raknet.protocol.packet.*;

/**
 * Represents a JRakLib Client.
 * @author jython234
 */
public class JRakLibClient extends Thread{
    private static long clientID = new Random(System.currentTimeMillis()).nextLong();
    private static long startTime = -1;
    protected InetSocketAddress serverEndpoint;

    protected Logger logger;
    protected boolean shutdown = false;

    protected List<byte[]> externalQueue;
    protected List<byte[]> internalQueue;

    /**
     * Creates a new JRakLibClient and connects right away.
     * @param logger Logger implementation for the client.
     * @param serverIP The server IP address.
     * @param serverPort The server's port.
     */
    public JRakLibClient(Logger logger, String serverIP, int serverPort){
        if(serverPort < 1 || serverPort > 65536){
            throw new IllegalArgumentException("Invalid port range.");
        }
        this.logger = logger;
        this.serverEndpoint = new InetSocketAddress(serverIP, serverPort);

        externalQueue = new LinkedList<>();
        internalQueue = new LinkedList<>();

        start();
    }

    /**
     * Pings a RakNet server at the specified <code>serverIP</code> and <code>serverPort</code>.
     * @param logger Logger implementation.
     * @param serverIP The server's IP Address.
     * @param serverPort The server's port.
     * @param tries Amount of packets sent with no response before giving up. (tries)
     * @param delay Delay between packets sent with no response. (In milliseconds)
     * @throws IOException If there is a problem while reading/sending.
     * @return The ping response
     */
    public static PingResponse pingServer(Logger logger, String serverIP, int serverPort, int tries, int delay) throws IOException {
        if(startTime == -1){
            startTime = System.currentTimeMillis();
        }
        UDPClientSocket socket = new UDPClientSocket(logger);
        for(int i = 0; i < tries; i++){
            UNCONNECTED_PING ping = new UNCONNECTED_PING();
            ping.pingID = System.currentTimeMillis() - startTime; //Amount since start.
            ping.encode();
            socket.writePacket(ping.buffer, new InetSocketAddress(serverIP, serverPort));
            socket.close();
            DatagramPacket pkt = socket.readPacketBlocking(delay);
            if(pkt != null && pkt.getData()[0] == UNCONNECTED_PONG.ID){
                UNCONNECTED_PONG pong = new UNCONNECTED_PONG();
                pong.buffer = pkt.getData();
                pong.decode();
                return new PingResponse(pong.serverID, pong.pingID, pong.serverName);
            }
        }
        return null;
    }

    public boolean isShutdown(){
        return shutdown == true;
    }

    public void shutdown(){
        shutdown = true;
    }

    public int getServerPort(){
        return serverEndpoint.getPort();
    }

    public String getServerIP(){
        return serverEndpoint.getHostString();
    }

    public InetSocketAddress getServerEndpoint() {
        return serverEndpoint;
    }

    public Logger getLogger(){
        return logger;
    }

    public List<byte[]> getExternalQueue(){
        return externalQueue;
    }

    public List<byte[]> getInternalQueue(){
        return internalQueue;
    }

    public void pushMainToThreadPacket(byte[] bytes){
        internalQueue.add(0, bytes);
    }

    public byte[] readMainToThreadPacket(){
        if(!internalQueue.isEmpty()) {
            byte[] data = internalQueue.get(internalQueue.size() - 1);
            internalQueue.remove(data);
            return data;
        }
        return null;
    }

    public void pushThreadToMainPacket(byte[] bytes){
        externalQueue.add(0, bytes);
    }

    public byte[] readThreadToMainPacket(){
        if(!externalQueue.isEmpty()) {
            byte[] data = externalQueue.get(externalQueue.size() - 1);
            externalQueue.remove(data);
            return data;
        }
        return null;
    }

    /**
     * Regenerates the static clientID.
     */
    public static void regenerateClientID(){
        clientID = new Random().nextLong();
    }

    /**
     * Regenerates the static clientID with the specified seed.
     * @param seed The seed to generate the clientID.
     */
    public static void regenerateClientID(long seed){
        clientID = new Random(seed).nextLong();
    }

    public static long getClientID(){
        return clientID;
    }

    public long getTimeSinceStart() {
        return startTime;
    }

    private class ShutdownHandler extends Thread {
        @Override
        public void run() {
            if (shutdown != true) {
                logger.severe("[JRakLibClient Thread #" + getId() + "] JRakLib crashed!");
            }
        }
    }

    /**
     * Represents a PingResponse from a server.
     */
    public static class PingResponse {
        /**
         * The PingId from the packet. The original amount sent to the server was the time sent start. (in milliseconds)
         * <br>
         * You can find more information here: http://wiki.vg/Pocket_Minecraft_Protocol#ID_CONNECTED_PING_OPEN_CONNECTIONS_.280x01.29
         */
        public final long pingId;
        /**
         * The serverId from the packet. This is the server's unique identifier generated at startup. (The value changes each runtime, and could be modified in transport)
         */
        public final long serverId;
        /**
         * The server's name or MOTD.
         */
        public final String name;

        public PingResponse(long serverId, long pingId, String name){
            this.pingId = pingId;
            this.serverId = serverId;
            this.name = name;
        }
    }

    @Override
    public void run(){
        setName("JRakLib Client Thread #"+getId());
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
        UDPClientSocket socket = new UDPClientSocket(logger);
        new ConnectionManager(this, socket);
    }
}
