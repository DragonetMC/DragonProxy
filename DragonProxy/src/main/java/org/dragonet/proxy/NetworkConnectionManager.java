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

import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.adapter.ClientProtocolAdapter;

/**
 *
 * @author robotman3000
 */
public class NetworkConnectionManager {

    @Getter
    private SessionRegister sessionRegister;

    @Getter
    @Setter
    private String motd = "<No MOTD>";

    private final DragonProxy proxy;
    private final ClientProtocolAdapter clientNetwork;

    public NetworkConnectionManager(DragonProxy proxy, ClientProtocolAdapter clientNetwork) {
        this.proxy = proxy;
        this.sessionRegister = new SessionRegister(proxy);
        this.clientNetwork = clientNetwork;
    }

    public void onTick() {
        clientNetwork.onTick();
        sessionRegister.onTick();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
