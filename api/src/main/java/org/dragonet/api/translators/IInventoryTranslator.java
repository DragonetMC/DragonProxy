package org.dragonet.api.translators;

import org.dragonet.api.caches.ICachedWindow;
import org.dragonet.api.sessions.IUpstreamSession;

/**
 * Represents an inventory translator.
 */
public interface IInventoryTranslator {

    /**
     * Prepare a window for the Bedrock client, spawn fake blocks.
     *
     * @param session the upstream session.
     * @param window the inventory window.
     * @return true if that window can be opened on bedrock edition successfully.
     */
    boolean prepare(IUpstreamSession session, ICachedWindow window);

    /**
     * Opens a window on the Bedrock client.
     *
     * @param session the upstream session.
     * @param window the inventory window.
     * @return true if that window can be opened on bedrock edition successfully.
     */
    boolean open(IUpstreamSession session, ICachedWindow window);

    /**
     * Update a window's content.
     *
     * @param session the upstream session.
     * @param window the inventory window.
     */
    void updateContent(IUpstreamSession session, ICachedWindow window);

    /**
     * Update a single slot in a window.
     *
     * @param session the upstream session.
     * @param window the inventory window.
     * @param slotIndex the index of the slot.
     */
    void updateSlot(IUpstreamSession session, ICachedWindow window, int slotIndex);
}
