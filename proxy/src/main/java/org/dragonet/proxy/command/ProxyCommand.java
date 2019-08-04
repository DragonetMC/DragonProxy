/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dragonet.proxy.network.session.ProxySession;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ProxyCommand {
    private String name;
    private String description;
    private String usage;

    // TOOD: support for executing commands from console, but for now we will just keep DragonConsole
    public abstract void execute(ProxySession session, String[] arguments);
}
