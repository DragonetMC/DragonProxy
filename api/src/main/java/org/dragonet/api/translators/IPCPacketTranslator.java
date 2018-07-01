package org.dragonet.api.translators;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 * Represents a Java to Bedrock edition packet translator.
 *
 * @param <P> the original Java edition packet class.
 */
public interface IPCPacketTranslator<P extends Packet> {

    /**
     * Translate a packet from Java to Bedrock edition.
     *
     * @param session the upstream session.
     * @param packet the packet.
     * @return the resulting translated packet array.
     */
    PEPacket[] translate(IUpstreamSession session, P packet);
}
