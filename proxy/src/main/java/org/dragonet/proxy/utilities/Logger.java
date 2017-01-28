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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.dragonet.proxy.DragonProxy;

public class Logger {

    private DragonProxy proxy;

    private Calendar calender;
    private SimpleDateFormat consoleDate;

    public boolean debug = false;

	private File logFile;
	FileOutputStream logOut;
	private static final byte[] NEW_LINE = "\n".getBytes();

    public Logger(DragonProxy proxy, File logFile) {
        this.proxy = proxy;
        calender = Calendar.getInstance();
        consoleDate = new SimpleDateFormat("HH:mm:ss");
        if(logFile != null){
        	try{
        		if(logFile.isFile()){
            		if(logFile.exists()){
            			logFile.delete();
            		}
            		logFile.createNewFile();
            		logOut = new FileOutputStream(logFile);
            		
            		// Setting the variable here ensures that it will not be null
            		// if, and only if everything else goes well
            		this.logFile = logFile; 
        		}
        	} catch (Exception e){
        		this.logFile = null;
        		e.printStackTrace();
        	}
        }
    }

    private void log(String level, String message) {
        log(MCColor.WHITE, level, message);
    }

    private void log(String levelColor, String level, String message) {
        StringBuilder builder = new StringBuilder();

        builder.append(MCColor.toANSI(MCColor.AQUA + "[" + consoleDate.format(calender.getTime()) + "] "));
        builder.append(MCColor.toANSI(levelColor + "[" + level + "] "));
        builder.append(MCColor.toANSI(message + MCColor.WHITE + MCColor.RESET));

        String logMessage = builder.toString();
        System.out.println(logMessage);
        
        if(logFile != null){
        	try {
        		logOut.write(logMessage.getBytes());
				logOut.write(NEW_LINE);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    public void info(String message) {
        log(MCColor.RESET, "INFO", message);
    }

    public void warning(String message) {
        log(MCColor.YELLOW, "WARNING", message);
    }

    public void severe(String message) {
        log(MCColor.RED, "SEVERE", message);
    }

    public void debug(String message) {
        if(debug) log(MCColor.DARK_GRAY, "DEBUG", message);
    }
}
