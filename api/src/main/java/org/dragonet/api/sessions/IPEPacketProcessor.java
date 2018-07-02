package org.dragonet.api.sessions;

import org.dragonet.api.network.PEPacket;

/**
 * Represents a Bedrock edition packet processor.
 */
public interface IPEPacketProcessor {

    /**
     * Returns the current Bedrock edition session.
     *
     * @return the Bedrock edition session.
     */
    IUpstreamSession getClient();

    /**
     * Put a raw packet data into the buffer.
     *
     * @param packet the packet byte array
     */
    void putPacket(byte[] packet);

    /**
     * The processor tick handler.
     */
    void onTick();

    /**
     * Handles a Bedrock edition packet.
     * TODO: this method should be in UpstreamSession
     *
     * @param packet the Bedrock edition packet.
     */
    void handlePacket(PEPacket packet);

    /**
     * Enables/disables the packet forward mode.
     *
     * @param enabled if set to true the forward mode will be activated.
     */
    void setPacketForwardMode(boolean enabled);
}
