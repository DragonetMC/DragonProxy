package org.dragonet.common.mcbedrock.protocol.packets;

import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.ProtocolInfo;

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
