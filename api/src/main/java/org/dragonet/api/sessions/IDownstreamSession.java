/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
