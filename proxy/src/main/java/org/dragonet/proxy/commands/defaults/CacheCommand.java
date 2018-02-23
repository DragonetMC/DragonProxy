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
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.network.UpstreamSession;

public class CacheCommand extends Command {

    public CacheCommand(String name) {
        super(name, "Cache command, display cached objects info");
    }

    public void execute(DragonProxy proxy, String[] args) {
        if (args.length == 0)
        {
            if (DragonProxy.getInstance().getSessionRegister().getAll().isEmpty()) {
                DragonProxy.getInstance().getLogger().info("No session, no cache");
                return;
            }
            DragonProxy.getInstance().getLogger().info("Display cache info per session");
            for(UpstreamSession session : DragonProxy.getInstance().getSessionRegister().getAll().values()) {
                DragonProxy.getInstance().getLogger().info("Session: " + session.getRaknetID() + " (" + session.getUsername() + ")");
                DragonProxy.getInstance().getLogger().info("\tentities :" + session.getEntityCache().getEntities().size());
                DragonProxy.getInstance().getLogger().info("\tchunks :" + session.getChunkCache().getChunks().size());
            }
        }
    }
}
