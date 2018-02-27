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

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.utilities.Logger;

import java.util.Scanner;

public class ConsoleCommandReader {

    private final Logger logger;
    private final DragonProxy proxy;

    public ConsoleCommandReader(DragonProxy proxy) {
        this.proxy = proxy;
        this.logger = proxy.getLogger();
    }

    public void startConsole() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String command = "";
                while (!proxy.isShuttingDown()) {
                    try {
//                        System.out.print(">");
                        command = new Scanner(System.in).nextLine();

                        if (command == null || command.trim().length() == 0) {
                            continue;
                        }
                        proxy.getLogger().info("[Console] Execute command /" + command);
                        proxy.getCommandRegister().callCommand(command);
                    } catch (Exception ex) {
                        logger.fatal("Error while executing command: " + ex);
                        ex.printStackTrace();
                    }
                }
            }
        });

        thread.setName("ConsoleCommandThread");
        thread.setDaemon(true);
        thread.start();
    }
}
