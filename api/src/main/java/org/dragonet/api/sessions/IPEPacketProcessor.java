package org.dragonet.api.sessions;

import org.dragonet.api.network.PEPacket;

/**
 * Represents a Bedrock edition packet processor.
 *
 * TODO: documentation
 */
public interface IPEPacketProcessor {

    /**
     * Returns the current Bedrock edition session.
     *
     * @return the Bedrock edition session.
     */
    IUpstreamSession getClient();

    void putPacket(byte[] packet);

    void onTick();

    // TODO: this method should be in UpstreamSession
    void handlePacket(PEPacket packet);

    void setPacketForwardMode(boolean enabled);
}
