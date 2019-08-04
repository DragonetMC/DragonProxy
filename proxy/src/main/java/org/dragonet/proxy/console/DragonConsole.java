/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.console;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializers;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.dragonet.proxy.DragonProxy;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DragonConsole extends SimpleTerminalConsole {

    @Inject
    private DragonProxy proxy;
    @Inject
    private Logger logger;

    DragonConsole() {
    }

    public void sendMessage(@NonNull Component component) {
        logger.info(ComponentSerializers.LEGACY.serialize(component));
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder.appName("DragonProxy"));
    }

    @Override
    protected boolean isRunning() {
        return proxy.isRunning();
    }

    @Override
    public void runCommand(String command) {
        switch (command.toLowerCase()) {
            case ("stop"): {
                proxy.shutdown();
                break;
            }
            case ("help"): {
                logger.info("\nCommands:\nstop: stop the DragonProxy server\nhelp: show this page");
                break;
            }
            case "kill":
                System.exit(0);
                break;
            default: {
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
