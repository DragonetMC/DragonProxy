/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;

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
