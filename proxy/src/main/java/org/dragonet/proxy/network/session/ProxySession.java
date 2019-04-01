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
package org.dragonet.proxy.network.session;

import com.nukkitx.protocol.PlayerSession;
import org.dragonet.proxy.util.RemoteServer;

public abstract class ProxySession implements PlayerSession, AutoCloseable {

    public abstract RemoteServer getRemoteServer();

    public abstract void setRemoteServer(RemoteServer s);
}
