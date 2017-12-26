package org.dragonet.proxy.protocol.packets;

import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.ProtocolInfo;

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
