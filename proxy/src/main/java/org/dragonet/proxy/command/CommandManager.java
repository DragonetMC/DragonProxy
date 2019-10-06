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
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.command;

import lombok.Getter;
import org.dragonet.proxy.command.defaults.StatsCommand;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CommandManager {
    private Map<String, ProxyCommand> commands = new HashMap<>();

    public CommandManager() {
        commands.put("stats", new StatsCommand());
    }

    public void executeCommand(ProxySession session, String command) {
        String name = command.trim().toLowerCase();
        String[] args;

        if (!name.contains(" ")) {
            name = name.toLowerCase();
            args = new String[0];
        } else {
            name = name.substring(0, name.indexOf(" ")).toLowerCase();
            String argLine = name.substring(name.indexOf(" " + 1));
            args = argLine.contains(" ") ? argLine.split(" ") : new String[] { argLine };
        }

        ProxyCommand cmd = commands.get(name);
        if (cmd == null) {
            session.sendMessage("Invalid Command! Try /help for a list of commands.");
            return;
        }

        cmd.execute(session, args);
    }
}
