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

public class ConsoleCommandReader {
	// vars
	private final Logger logger;
	private final DragonProxy proxy;

	// constructor
	public ConsoleCommandReader(DragonProxy proxy) {
		this.proxy = proxy;
		this.logger = proxy.getLogger();
	}

	// public
	public void startConsole() {
		/*Thread thread = new Thread(new Runnable() {
			public void run() {
				String command = "";
				while (!proxy.isShuttingDown()) {
					try {
						System.out.print(">");
						command = System.console().readLine();

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
		});

		thread.setName("ConsoleCommandThread");
		thread.setDaemon(true);
		thread.start();*/
	}

	// private

}
