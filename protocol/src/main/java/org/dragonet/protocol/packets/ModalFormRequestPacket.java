package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

/**
 * Created on 2017/12/26.
 */
public class ModalFormRequestPacket extends PEPacket {

    public int formId;
    public String formData;

    @Override
    public int pid() {
        return ProtocolInfo.MODAL_FORM_REQUEST_PACKET;
    }

    @Override
    public void encodePayload() {
        putUnsignedVarInt(formId);
        putString(formData);
    }

    @Override
    public void decodePayload() {
        formId = (int) (getUnsignedVarInt() & 0xFFFFFFFF);
        formData = getString();
    }
}
