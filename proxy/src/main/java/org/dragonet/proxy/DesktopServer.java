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
package org.dragonet.proxy;

import java.util.Map;

import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.adapter.MCPCServerProtocolAdapter;
import org.dragonet.proxy.network.adapter.ServerProtocolAdapter;

public class DesktopServer extends RemoteServer {

    private MCPCServerProtocolAdapter protocol;
    
    public static DesktopServer deserialize(Map<String, Object> map) {
        return (DesktopServer) delicatedDeserialize(new DesktopServer(), map);
    }

    @Override
    public final ServerProtocolAdapter getProtocolAdapter(ClientConnection session) {
        if(protocol == null){
            protocol = new MCPCServerProtocolAdapter();
            protocol.setClient(session);
        }   
        return protocol;
    }
}

