package org.dragonet.dragonproxy.proxy;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class DragonProxyMain {

    public static void main(String[] args) {
        // Check the java version
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0) {
            System.err.println("DragonProxy requires Java 8! Current version: " + System.getProperty("java.version"));
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
            System.out.println("Version: " + DragonProxyMain.class.getPackage().getImplementationVersion());
            return;
        }
        int bedrockPort = options.has(bedrockPortOption) ? Integer.parseInt(options.valueOf(bedrockPortOption)) : -1;
        int javaPort = options.has(javaPortOption) ? Integer.parseInt(options.valueOf(bedrockPortOption)) : -1;
    }
}
