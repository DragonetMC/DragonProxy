/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 *
 * @author Epic
 */
public class ServerSettingsRequestPacket extends PEPacket {

    public ServerSettingsRequestPacket() {
    }

    @Override
    public int pid() {
        return ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET;
    }

    @Override
    public void encodePayload() {
        //No payload
    }

    @Override
    public void decodePayload() {
        //No payload
    }
}
