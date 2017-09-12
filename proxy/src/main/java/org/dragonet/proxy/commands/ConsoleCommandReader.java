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
package org.dragonet.proxy.commands;

import java.io.IOException;

import jline.console.ConsoleReader;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.utilities.Logger;

public class ConsoleCommandReader {

    private final Logger logger;

    private final DragonProxy proxy;

    private ConsoleReader reader;

    public ConsoleCommandReader(DragonProxy proxy) {
        this.proxy = proxy;
        this.logger = proxy.getLogger();

        try {
            reader = new ConsoleReader();
        } catch (IOException ex) {
            logger.severe("Exception initializing console reader: " + ex);
        }
    }

    public void startConsole() {
        Thread thread = new ConsoleCommandThread();
        thread.setName("ConsoleCommandThread");
        thread.setDaemon(true);
        thread.start();
    }


    private class ConsoleCommandThread extends Thread {

        @Override
        public void run() {
            String command = "";
            while (!proxy.isShuttingDown()) {
                try {
                    command = reader.readLine(">", null);

                    if (command == null || command.trim().length() == 0) {
                        continue;
                    }

                    proxy.getCommandRegister().callCommand(command);
                } catch (Exception ex) {
                    logger.severe("Error while executing command: " + ex);
                    ex.printStackTrace();
                }
            }
        }
    }
}
