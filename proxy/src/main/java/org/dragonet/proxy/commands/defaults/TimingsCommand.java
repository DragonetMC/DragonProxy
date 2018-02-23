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
package org.dragonet.proxy.commands.defaults;

import co.aikar.timings.Timings;
import co.aikar.timings.TimingsExport;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;

public class TimingsCommand extends Command {

    public TimingsCommand(String name) {
        super(name, "Timings command (start / stop / paste / history)");
    }

    public void execute(DragonProxy proxy, String[] args) {
        if (args.length == 0) {
            proxy.getLogger().info("Timings command (start / stop / paste / history)");
        }
        else {
            switch(args[0]) {
                case "start":
                    Timings.setTimingsEnabled(true);
                    proxy.getLogger().info("Timings started !");
                    break;
                case "stop":
                    Timings.setTimingsEnabled(false);
                    proxy.getLogger().info("Timings stopped !");
                    break;
                case "reset":
                    Timings.reset();
                    proxy.getLogger().info("Timings reseted !");
                    break;
                case "paste":
                    TimingsExport.reportTimings(null);
                    break;
                case "history":
                    break;
            }
        }
    }
}
