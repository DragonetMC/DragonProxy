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

    public boolean debug = false;

    public Logger(DragonProxy proxy) {
        proxy = proxy;
        calender = Calendar.getInstance();
        consoleDate = new SimpleDateFormat("HH:mm:ss");
    }

    private void log(String level, String message) {
        log(MCColor.WHITE, level, message);
    }

    private void log(String levelColor, String level, String message) {
        StringBuilder builder = new StringBuilder();

        builder.append(MCColor.toANSI(MCColor.AQUA + "[" + consoleDate.format(calender.getTime()) + "] "));
        builder.append(MCColor.toANSI(levelColor + "[" + level + "] "));
        builder.append(MCColor.toANSI(message + MCColor.WHITE + MCColor.RESET));

        System.out.println(builder.toString());
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warning(String message) {
        log(MCColor.YELLOW, "WARNING", message);
    }

    public void severe(String message) {
        log(MCColor.RED, "SEVERE", message);
    }

    public void debug(String message) {
        if(debug) log(MCColor.GRAY, "DEBUG", message);
    }
}
