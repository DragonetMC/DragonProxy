
package org.dragonet.api.translators;

import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 * Represents a Bedrock to Java edition packet translator.
 *
 * @param <P> the original Bedrock edition packet class.
 */
public interface IPEPacketTranslator<P extends PEPacket> {

    /**
     * Translate a packet from Bedrock to Java edition.
     *
     * @param session the upstream session.
     * @param packet the packet.
     * @return the resulting translated packet array.
     */
    Packet[] translate(IUpstreamSession session, P packet);
}
