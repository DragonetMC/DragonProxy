/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
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
        switch (command.toLowerCase()){
            case ("stop"):{
                proxy.shutdown();
                break;
            }
            case ("help"):{
                logger.info("\nCommands:\nstop: stop the DragonProxy server\nhelp: show this page");
                break;
            }
            default:{
                logger.info("Unknown command: " + command);
                break;
            }
        }
    }

    @Override
    protected void shutdown() {
        proxy.shutdown();
    }
}
