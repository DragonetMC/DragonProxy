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

// Only use if you have to. Clients will eventually timeout.
public class KillCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        proxy.getLogger().info("Forcefully killing proxy...");
        System.exit(0);
    }
}
