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

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.ConsoleCommand;

public class HelpCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
		proxy.getLogger().info("---- All commands for DragonProxy ----");
		proxy.getLogger().info("help - Show this help page");
		proxy.getLogger().info("stop - Stop the proxy!");
        proxy.getLogger().info("kill - Kill the proxy");
        proxy.getLogger().info("test - For testing only\n");
    }
}
