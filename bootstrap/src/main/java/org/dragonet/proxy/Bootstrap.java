/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Bootstrap {

    public static void main(String[] args) {
        log.info("Starting DragonProxy...");

        // Check the java version
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            log.error("DragonProxy requires Java 8! Current version: " + System.getProperty("java.version"));
            return;
        }

        // Define command-line options
        OptionParser optionParser = new OptionParser();
        optionParser.accepts("version", "Displays the proxy version");
        OptionSpec<String> bedrockPortOption = optionParser.accepts("bedrockPort", "Overrides the bedrock UDP bind port").withRequiredArg();
        OptionSpec<String> javaPortOption = optionParser.accepts("javaPort", "Overrides the java TCP bind port").withRequiredArg();
        optionParser.accepts("help", "Display help/usage information").forHelp();

        // Handle command-line options
        OptionSet options = optionParser.parse(args);
        if (options.has("version")) {
            log.info("Version: " + Bootstrap.class.getPackage().getImplementationVersion());
            return;
        }

        int bedrockPort = options.has(bedrockPortOption) ? Integer.parseInt(options.valueOf(bedrockPortOption)) : -1;
        int javaPort = options.has(javaPortOption) ? Integer.parseInt(options.valueOf(bedrockPortOption)) : -1;

        DragonProxy proxy = new DragonProxy(bedrockPort, javaPort);

        Runtime.getRuntime().addShutdownHook(new Thread(proxy::shutdown, "Shutdown thread"));
    }
}
