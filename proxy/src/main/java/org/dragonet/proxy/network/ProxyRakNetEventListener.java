/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network;

import com.nukkitx.network.raknet.RakNetServerEventListener;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.DragonConfiguration;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

public class ProxyRakNetEventListener implements RakNetServerEventListener {

    @Nonnull
    @Override
    public Action onConnectionRequest(InetSocketAddress address, int protocolVersion) {
        System.out.println("RakNet version: " + protocolVersion);
        return Action.CONTINUE;
    }

    @Nonnull
    public Advertisement onQuery(InetSocketAddress address) {
        DragonConfiguration config = DragonProxy.INSTANCE.getConfiguration();
        return new Advertisement("MCPE", config.getMotd(), 332, "1.9.0", 0, config.getMaxPlayers(), config.getMotd2(), "SMP");
    }
}
