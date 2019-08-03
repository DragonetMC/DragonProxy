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
