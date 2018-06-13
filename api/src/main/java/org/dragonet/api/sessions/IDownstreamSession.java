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
package org.dragonet.api.sessions;

import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;

public interface IDownstreamSession<Packet> {

    public void connect(String addr, int port);

    public void disconnect();

    public boolean isConnected();

    public void send(Packet packet);

    public void send(Packet... packets);

    public void sendChat(String chat);

    public void onPacketSending(PacketSendingEvent event);

    public void onPacketRecieved(PacketReceivedEvent event);

    public void onTick();
}
