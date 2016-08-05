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


import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * A UDP Client socket.
 *
 * @author jython234
 */
public class UDPClientSocket implements Closeable{
    private Logger logger;
    private DatagramSocket socket;

    /**
     * Create a new UDP Client socket with the specified logger.
     * @param logger The logger used in case the socket was unable to be created.
     */
    public UDPClientSocket(Logger logger){
        this.logger = logger;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.setSendBufferSize(1024 * 1024 * 8);
            socket.setReceiveBufferSize(1024 * 1024 * 8);
            socket.setSoTimeout(1);
        } catch (SocketException e) {
            logger.severe("**** FAILED TO CREATE SOCKET!");
            logger.severe("java.net.SocketException: "+e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns the socket used by this client socket.
     * @return DatagramSocket
     */
    public DatagramSocket getSocket(){
        return socket;
    }

    /**
     * Closes this socket, and the underlying DatagramSocket.
     */
    @Override
    public void close() {
        socket.close();
    }

    /**
     * Reads a packet from the socket (non-blocking). If no packet is received, the method will return <code>null.</code>
     * @return The DatagramPacket, if received.
     * @throws IOException If there is a problem while reading.
     */
    public DatagramPacket readPacket() throws IOException {
        DatagramPacket dp = new DatagramPacket(new byte[65535], 65535);
        try {
            socket.receive(dp);
            dp.setData(Arrays.copyOf(dp.getData(), dp.getLength()));
            return dp;
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    /**
     * Reads a packet from the socket (blocking). If no packet is received, the method will return <code>null.</code>
     * @param blockFor Block for this amount (milliseconds). If amount is negative, the method will block forever (until a packet is received)
     * @return The DatagramPacket, if received.
     * @throws IOException If there is a problem while reading.
     */
    public DatagramPacket readPacketBlocking(int blockFor) throws IOException{
        DatagramPacket dp = new DatagramPacket(new byte[65535], 65535);
        try {
            socket.setSoTimeout(blockFor);
            socket.receive(dp);
            socket.setSoTimeout(1);
            dp.setData(Arrays.copyOf(dp.getData(), dp.getLength()));
            return dp;
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    /**
     * Sends a packet to the specified <code>dest</code>.
     * @param buffer Raw payload of the packet.
     * @param dest The endpoint where the data will go.
     * @throws IOException If there is a problem while sending.
     */
    public void writePacket(byte[] buffer, SocketAddress dest) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, dest);
        socket.send(dp);
    }

    /**
     * Sets this socket's send buffer size. Defaults to 1024 * 1024 * 8.
     * @param size Size in bytes.
     * @throws SocketException If there is a failure.
     */
    public void setSendBuffer(int size) throws SocketException {
        socket.setSendBufferSize(size);
    }

    /**
     * Sets this socket's receive buffer size. Defaults to 1024 * 1024 * 8.
     * @param size Size in bytes.
     * @throws SocketException If there is a failure.
     */
    public void setRecvBuffer(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
    }
}
