/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
