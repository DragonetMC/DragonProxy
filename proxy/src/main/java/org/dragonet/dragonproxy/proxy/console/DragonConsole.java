package org.dragonet.dragonproxy.proxy.console;

import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.dragonet.dragonproxy.proxy.DragonProxy;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import javax.inject.Inject;

public class DragonConsole extends SimpleTerminalConsole {

    @Inject
    private DragonProxy proxy;

    DragonConsole() {
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder.appName("DragonProxy"));
    }

    @Override
    protected boolean isRunning() {
        return !proxy.isShutdown();
    }

    @Override
    protected void runCommand(String command) {
        if(command.equalsIgnoreCase("stop")) {
            proxy.shutdown();
        }
    }

    @Override
    protected void shutdown() {
        proxy.shutdown();
    }
}
