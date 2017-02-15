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

public interface DownstreamSession<PACKET> {
    
    public void connect(String addr, int port);
    
    public boolean isConnected();
    
    public void onTick();

    public void send(PACKET packet);

    public void send(PACKET... packets);
    
    public void sendChat(String chat);
    
    public void disconnect();
}
