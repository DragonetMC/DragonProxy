package org.dragonet.raknet.protocol.packet;

import org.dragonet.raknet.protocol.Packet;

import java.net.InetSocketAddress;

/**
 * author: MagicDroidX Nukkit Project
 */
public class CLIENT_HANDSHAKE_DataPacket extends Packet {

    public static byte ID = (byte) 0x13;

    @Override
    public byte getID() {
        return ID;
    }

    public String address;
    public int port;
    public InetSocketAddress[] systemAddresses = new InetSocketAddress[10];

    public long sendPing;
    public long sendPong;

    @Override
    public void encode() {
        super.encode();
        this.putAddress(new InetSocketAddress(address, port));
        for (int i = 0; i < 10; i++) {
            this.putAddress(systemAddresses[i]);
        }
        
        this.putLong(sendPing);
        this.putLong(sendPong);
    }

    @Override
    public void decode() {
        super.decode();
        InetSocketAddress addr = this.getAddress();
        this.address = addr.getHostString();
        this.port = addr.getPort();

        for (int i = 0; i < 10; i++) {
            this.systemAddresses[i] = this.getAddress();
        }

        this.sendPing = this.getLong();
        this.sendPong = this.getLong();
    }

}
