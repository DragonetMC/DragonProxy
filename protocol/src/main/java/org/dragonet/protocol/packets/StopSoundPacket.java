package org.dragonet.protocol.packets;

import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.ProtocolInfo;

public class StopSoundPacket extends PEPacket {

    public String name;
    public boolean stopAll = false;
    
    public StopSoundPacket() {

	}
	
	@Override
	public int pid() {
		return ProtocolInfo.STOP_SOUND_PACKET;
	}

	@Override
	public void encodePayload() {
		putString(name);
		putBoolean(stopAll);
	}

	@Override
	public void decodePayload() {
		name = getString();
		stopAll = getBoolean();
	}

}
