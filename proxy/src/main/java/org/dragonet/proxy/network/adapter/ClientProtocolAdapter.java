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

import java.util.UUID;
import org.dragonet.proxy.network.ClientConnection;

/** 
 * @author robotman3000
 */
public interface ClientProtocolAdapter<T> extends ProtocolAdapter<T> {
    public void sendPacket(T packet, ClientConnection id);

    public void clientDisconectRequest(ClientConnection id, String reason);

    public void onTick();
}
