package org.dragonet.api.sessions;

import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.packet.Packet;

/**
 * Represents the Java edition session.
 */
public interface IDownstreamSession {

    /**
     * Connects the Java edition session to the specified server.
     *
     * @param address the server address.
     * @param port    the server TCP port number.
     */
    void connect(String address, int port);

    /**
     * Disconnects the Java edition session from the actual server.
     */
    void disconnect();

    /**
     * Returns if the Java session is connected.
     *
     * @return true if the Java session is connected.
     */
    boolean isConnected();

    /**
     * Sends a packet to the connected Java edition server.
     *
     * @param packet the Java edition packet to be sent.
     */
    void send(Packet packet);

    /**
     * Sends packets to the connected Java edition server.
     *
     * @param packets the Java edition packets to be sent.
     */
    void send(Packet... packets);


    /**
     * Sends a chat packet to the connected Java edition server.
     *
     * @param chat the chat text to be sent.
     */
    void sendChat(String chat);

    /**
     * The Java session packet sending handler.
     *
     * @param event the packet sending event.
     */
    void onPacketSending(PacketSendingEvent event);

    /**
     * The Java session packet received handler.
     *
     * @param event the packet received event.
     */
    void onPacketReceived(PacketReceivedEvent event);

    /**
     * The Java session tick handler.
     */
    void onTick();
}
