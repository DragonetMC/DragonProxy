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

import org.dragonet.proxy.DragonProxy;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    private DragonProxy proxy;

    private Calendar calender;
    private SimpleDateFormat consoleDate;

    public Logger(DragonProxy proxy) {
        proxy = proxy;
        calender = Calendar.getInstance();
        consoleDate = new SimpleDateFormat("HH:mm:ss");
    }

    private void log(String level, String message) {
        log(Terminal.WHITE, level, message);
    }

    private void log(String levelColor, String level, String message) {
        StringBuilder builder = new StringBuilder();

        builder.append(Terminal.CYAN + "[" + consoleDate.format(calender.getTime()) + "] ");
        builder.append(levelColor + "[" + level + "] ");
        builder.append(message + Terminal.WHITE);

        System.out.println(builder.toString());
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warning(String message) {
        log(Terminal.YELLOW, "WARNING", message);
    }

    public void severe(String message) {
        log(Terminal.RED, "SEVERE", message);
    }

    public void debug(String message) {
        return; // Temp fix because this threw an NPE
    }
}
