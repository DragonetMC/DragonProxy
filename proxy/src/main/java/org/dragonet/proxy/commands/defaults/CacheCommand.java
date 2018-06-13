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
import org.dragonet.proxy.DragonProxy;
import org.dragonet.api.commands.Command;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.proxy.network.UpstreamSession;

public class CacheCommand extends Command {

    public CacheCommand(String name) {
        super(name, "Cache command, display cached objects info");
    }

    @Override
    public void execute(ProxyServer proxy, String[] args) {
        if (args.length == 0) {
            if (proxy.getSessionRegister().getAll().isEmpty()) {
                proxy.getLogger().info("No session, no cache");
                return;
            }
            proxy.getLogger().info("Display cache info per session");
            for(IUpstreamSession session : proxy.getSessionRegister().getAll().values()) {
                proxy.getLogger().info("Session: " + session.getRaknetID() + " (" + session.getUsername() + ")");
                proxy.getLogger().info("\tentities :" + session.getEntityCache().getEntities().size());
                proxy.getLogger().info("\tchunks :" + session.getChunkCache().getChunks().size());
            }
        }
    }
}
