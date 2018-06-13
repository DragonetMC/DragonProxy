/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.sessions;

import org.dragonet.api.ProxyServer;

/**
 *
 * @author Epic
 */
public interface IRaknetInterface {

    public ProxyServer getProxy();

    public String getServerName();

    public int getMaxPlayers();

    public void setBroadcastName(String serverName, int players, int maxPlayers);

    public void shutdown();
}
