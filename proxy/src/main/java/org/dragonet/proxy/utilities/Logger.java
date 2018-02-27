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

import org.apache.logging.log4j.LogManager;
import org.dragonet.proxy.DragonProxy;

public class Logger {

    private DragonProxy proxy;
    private final org.apache.logging.log4j.Logger logger;
    public boolean debug = false;

    public Logger(DragonProxy proxy) {
        this.proxy = proxy;
        logger = LogManager.getLogger(DragonProxy.class);
        //todo enable or disable log file
    }

    public void info(String message) {
      logger.info(message);
    }

    public void warning(String message) {
      logger.warn(message);
    }

    public void error(String message) {
      logger.error(message);
    }

    public void fatal(String message) {
      logger.fatal(message);
    }

    public void debug(String message) {
        if (debug) {
            logger.debug(message);
        }
    }
}
