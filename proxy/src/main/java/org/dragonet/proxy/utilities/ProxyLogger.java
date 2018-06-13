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
package org.dragonet.proxy.utilities;

import io.sentry.Sentry;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.dragonet.api.logger.IProxyLogger;
import org.dragonet.proxy.DragonProxy;

public class ProxyLogger implements IProxyLogger {

    private final java.util.logging.Logger logger;
    private DragonProxy proxy;
    public boolean colorful = false;
    public boolean debug = false;

    public ProxyLogger(DragonProxy proxy) {
        this.proxy = proxy;
        this.logger = java.util.logging.Logger.getLogger(proxy.getClass().getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        //Redirect System.out to logger
        System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));

        //Console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT][%2$-5s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        logger.addHandler(consoleHandler);

        //File
        try {
            File logDir = new File("logs");
            logDir.mkdir();
            File logFile = new File(logDir, "latest.log");
            int maxLogFileSize = 20;//Mo
            if (logFile.exists() && (logFile.length()) > maxLogFileSize * 1024L * 1024L)
                logger.warning("Your log file is larger than " + maxLogFileSize + "Mo, you should backup and clean it !");
            FileHandler fileHandler = new FileHandler(logFile.getCanonicalPath(), true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT][%2$-5s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });
            logger.addHandler(fileHandler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(ProxyLogger.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Sentry
        if (System.getenv().containsKey("DP_SENTRY_CLIENT_KEY")) {
            Handler sentryHandler = new io.sentry.jul.SentryHandler();
            sentryHandler.setLevel(Level.SEVERE);
            logger.addHandler(sentryHandler);
            Sentry.init(System.getenv().get("DP_SENTRY_CLIENT_KEY"));
        }
    }

    @Override
    public void info(String message) {
        logger.info(org.dragonet.common.maths.MCColor.printConsole(message, proxy.getConfig().isLog_colors()));
    }

    @Override
    public void warning(String message) {
        logger.warning(org.dragonet.common.maths.MCColor.printConsole(message, proxy.getConfig().isLog_colors()));
    }

    @Override
    public void severe(String message) {
        logger.severe(org.dragonet.common.maths.MCColor.printConsole(message, proxy.getConfig().isLog_colors()));
    }

    @Override
    public void debug(String message) {
        if (debug)
            logger.info(org.dragonet.common.maths.MCColor.printConsole(message, proxy.getConfig().isLog_colors()));
    }

    @Override
    public void stop() {
        for (Handler handler : logger.getHandlers())
            handler.close();
    }
}
