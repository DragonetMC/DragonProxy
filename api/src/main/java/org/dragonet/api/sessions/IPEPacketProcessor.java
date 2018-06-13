/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.sessions;

import org.dragonet.api.network.PEPacket;

/**
 *
 * @author Epic
 */
public interface IPEPacketProcessor {

    public IUpstreamSession getClient();

    public void putPacket(byte[] packet);

    public void onTick();

    // this method should be in UpstreamSession
    public void handlePacket(PEPacket packet);

    public void setPacketForwardMode(boolean enabled);
}
