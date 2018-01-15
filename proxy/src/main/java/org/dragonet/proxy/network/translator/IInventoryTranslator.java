/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;

public interface IInventoryTranslator {

    /**
     * Prepare a window on MCPE, spawn fakeblocks.
     *
     * @param session
     * @param window
     * @return Can that window be opened on MCPE?
     */
    boolean prepare(UpstreamSession session, CachedWindow window);

    /**
     * Opens a window on MCPE.
     *
     * @param session
     * @param window
     * @return Can that window be opened on MCPE?
     */
    boolean open(UpstreamSession session, CachedWindow window);

    /**
     * Update a window's content.
     *
     * @param session
     * @param window
     */
    void updateContent(UpstreamSession session, CachedWindow window);

    /**
     * Update a single slot in a window.
     *
     * @param session
     * @param window
     * @param slotIndex
     */
    void updateSlot(UpstreamSession session, CachedWindow window, int slotIndex);
}
