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

import org.dragonet.api.ProxyServer;
import org.dragonet.api.commands.Command;

public class StopCommand extends Command {

    public StopCommand(String name) {
        super(name, "Stop the proxy");
    }

    @Override
    public void execute(ProxyServer proxy, String[] args) {
        proxy.shutdown();
    }
}
