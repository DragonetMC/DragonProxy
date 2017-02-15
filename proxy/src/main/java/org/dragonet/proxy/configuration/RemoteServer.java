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
package org.dragonet.proxy.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dragonet.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;
import lombok.Setter;

public abstract class RemoteServer implements ConfigurationSerializable {
    @Getter
    @Setter
    private String remoteAddr;
    
    @Getter
    @Setter
    private int remotePort;
    
    public void setRemote_addr(String remoteAddr) {
        setRemoteAddr(remoteAddr);
    }
    
    public void setRemote_port(int reportPort) {
        setRemotePort(reportPort);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("remote_addr", remoteAddr);
        map.put("remote_port", remotePort);
        return map;
    }
    
    /**
     * Required for deserailization. 
     * @param server 
     * @param map
     * @return 
     */
    public static RemoteServer delicatedDeserialize(RemoteServer server, Map<String, Object> map) {
        server.remoteAddr = (String) map.get("remote_addr");
        server.remotePort = ((Number) map.get("remote_port")).intValue();
        return server;
    }
}
