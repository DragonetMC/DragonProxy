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
import static org.dragonet.proxy.configuration.RemoteServer.delicatedDeserialize;

public class PocketServer extends RemoteServer {

    public static PocketServer deserialize(Map<String, Object> map) {
        return (PocketServer) delicatedDeserialize(new PocketServer(), map);
    }
}
