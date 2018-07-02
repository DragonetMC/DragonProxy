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
package org.dragonet.api.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pf4j.Plugin;

public class WrapedListener {

    private final Plugin plugin;
    private final Listener listener;
    private final Method method;

    public WrapedListener(Plugin plugin, Listener listener, Method method) {
        super();
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public void callEvent(Event event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Listener getListener() {
        return listener;
    }

}
