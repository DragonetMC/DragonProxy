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
package org.dragonet.proxy.commands.defaults;

import org.dragonet.api.commands.Command;
import org.dragonet.common.maths.MCColor;

import java.util.Map;
import java.util.TreeMap;
import org.dragonet.api.ProxyServer;

public class HelpCommand extends Command {

    public HelpCommand(String name) {
        super(name, "Displays commands for DragonProxy");
    }

    @Override
    public void execute(ProxyServer proxy, String[] args) {
        proxy.getLogger().info(MCColor.GREEN + "----[ All commands for DragonProxy ]----");

        Map<String, Command> commands = new TreeMap<>();
        for (Command cmd : proxy.getCommandRegister().getCommands().values()) {
            commands.put(cmd.getName(), cmd);
        }

        for (Command command1 : commands.values()) {
            proxy.getLogger().info(MCColor.DARK_GREEN + command1.getName() + ": " + MCColor.WHITE + command1.getDescription());
        }
    }
}
