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
import org.dragonet.proxy.network.adapter.MCPEServerProtocolAdapter;
import org.dragonet.proxy.network.adapter.ServerProtocolAdapter;

public class PocketServer extends RemoteServer {

    private MCPEServerProtocolAdapter protocol;
    
    public static PocketServer deserialize(Map<String, Object> map) {
        return (PocketServer) delicatedDeserialize(new PocketServer(), map);
    }

    @Override
    public final ServerProtocolAdapter getProtocolAdapter(ClientConnection session) {
        if(protocol == null){
            protocol = new MCPEServerProtocolAdapter();
            protocol.setClient(session);
        }    
        return protocol;
    }
}
