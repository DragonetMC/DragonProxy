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
package org.dragonet.proxy.network.adapter;
import org.dragonet.proxy.network.ClientConnection;

/**
 * @author robotman3000
 */
public interface ServerProtocolAdapter<T> extends ProtocolAdapter<T> {
    
    public void connectToRemoteServer(String address, int port);
    
    public void setClient(ClientConnection session);
    
    public void sendPacket(T packet);
}
