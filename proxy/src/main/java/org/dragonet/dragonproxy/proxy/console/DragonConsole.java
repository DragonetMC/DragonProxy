package org.dragonet.dragonproxy.proxy.console;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializers;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.dragonet.dragonproxy.api.command.CommandSource;
import org.dragonet.dragonproxy.proxy.DragonProxy;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DragonConsole extends SimpleTerminalConsole implements CommandSource {

    @Inject
    private DragonProxy proxy;
    @Inject
    private Logger logger;

    DragonConsole() {
    }

    @Override
    public void sendMessage(@NonNull Component component) {
        logger.info(ComponentSerializers.LEGACY.serialize(component));
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
