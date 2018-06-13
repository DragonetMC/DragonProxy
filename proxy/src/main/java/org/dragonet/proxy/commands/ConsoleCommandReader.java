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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dragonet.proxy.DragonProxy;

import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.commands.IConsoleCommandReader;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

public class ConsoleCommandReader implements IConsoleCommandReader {

    private final ProxyServer proxy;
    private final Terminal terminal;
    private Thread thread;

    public ConsoleCommandReader(ProxyServer proxy) {
        this.proxy = proxy;
        this.terminal = TerminalConsoleAppender.getTerminal();
    }

    @Override
    public void startConsole() {
        thread = new Thread() {
            @Override
            public void run() {
                if (terminal != null) {
                    LineReader reader = LineReaderBuilder.builder()
                            .appName("Example App") // TODO: Replace with your app name
                            .terminal(terminal)
                            .build(); // Important to make the appender aware of the reader
                    TerminalConsoleAppender.setReader(reader);

                    try {
                        String line;

                        while (true) {
                            try {
                                line = reader.readLine("> ");
                            } catch (EndOfFileException ignored) {
                                // This is thrown when the user indicates end of input using CTRL + D
                                // For most applications it doesn't make sense to stop reading input
                                // You can either disable console input at this point, or just continue
                                // reading normally.
                                continue;
                            }

                            if (line == null)
                                break;

                            // TODO: Execute command with the line
                        }
                    } catch (UserInterruptException e) {
                        // Called when CTRL + C is typed
                        // TODO: You should stop your app here
                    } finally {
                        // Note: At this point the `LineReader` is no longer readable
                        // The appender isn't aware of this so you should remove it manually to avoid errors
                        TerminalConsoleAppender.setReader(null);
                    }

                } else
                    // JLine isn't enabled or not supported
                    // TODO: Usually, you should fall back to reading from standard input here
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // TODO: Execute command with the line
                            proxy.getLogger().info("[Console] Executing command: " + line);
                            proxy.getCommandRegister().callCommand(line);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ConsoleCommandReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        };
        thread.setName("ConsoleCommandThread");
        proxy.getGeneralThreadPool().execute(thread);
    }
}
