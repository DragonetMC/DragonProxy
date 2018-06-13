/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 *
 * @author Epic
 */
public class ServerSettingsResponsePacket extends PEPacket {

    public long formId;
    public String formData;

    public ServerSettingsResponsePacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET;
    }

    @Override
    public void encodePayload() {
        this.putUnsignedVarInt(formId);
        this.putString(formData);
    }

    @Override
    public void decodePayload() {
        formId = getUnsignedVarInt();
        formData = getString();
    }
}
