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
package org.dragonet.api.commands;

import org.dragonet.api.ProxyServer;

public abstract class Command {

    private final String name;
    protected String description = "";

    // constructor
    public Command(String name) {
        this(name, "");
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(ProxyServer proxy, String[] args);

}
