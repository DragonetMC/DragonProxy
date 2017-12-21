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
package org.dragonet.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.*;
import org.dragonet.proxy.commands.CommandRegister;
import org.dragonet.proxy.commands.ConsoleCommandReader;

import org.yaml.snakeyaml.Yaml;

public class DragonProxy {

    public static final boolean IS_RELEASE = false; // DO NOT CHANGE, ONLY ON PRODUCTION

    private Logger logger;
    private final TickerThread ticker = new TickerThread(this);
    private ServerConfig config;
    private Lang lang;
    private SessionRegister sessionRegister;
    private RaknetInterface network;
    private boolean shuttingDown;
    private ScheduledExecutorService generalThreadPool;
    private CommandRegister commandRegister;
    private String authMode;
    private ConsoleCommandReader console;
    private String motd;
    private boolean debug = false;

    public static void main(String[] args) {
        new DragonProxy().run(args);
    }

    public Logger getLogger() {
        return logger;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public Lang getLang() {
        return lang;
    }

    public SessionRegister getSessionRegister() {
        return sessionRegister;
    }

    public RaknetInterface getNetwork() {
        return network;
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public ScheduledExecutorService getGeneralThreadPool() {
        return generalThreadPool;
    }

    public CommandRegister getCommandRegister() {
        return commandRegister;
    }

    public String getAuthMode() {
        return authMode;
    }

    public boolean isDebug() {
        return debug;
    }

    public void run(String[] args) {
        logger = new Logger(this);

        try {
            File fileConfig = new File("config.yml");
            if (!fileConfig.exists()) {
                // Create default config
                FileOutputStream fos = new FileOutputStream(fileConfig);
                InputStream ins = DragonProxy.class.getResourceAsStream("/config.yml");
                int data;
                while ((data = ins.read()) != -1) {
                    fos.write(data);
                }
                ins.close();
                fos.close();
            }
            config = new Yaml().loadAs(new FileInputStream(fileConfig), ServerConfig.class);
        } catch (IOException ex) {
            logger.severe("Failed to load configuration file! Make sure the file is writable.");
            ex.printStackTrace();
            return;
        }

        // Initialize console command reader
        console = new ConsoleCommandReader(this);
        console.startConsole();

        // Should we save console log? Set it in config file
        /*
		 * if(config.isLog_console()){ console.startFile("console.log");
		 * logger.info("Saving console output enabled"); } else {
		 * logger.info("Saving console output disabled"); }
         */
        // Put at the top instead
        if (!IS_RELEASE) {
            logger.warning(
                    MCColor.YELLOW + "This is a development build. It may contain bugs. Do not use on production.\n");
        }

        // Check for startup arguments
        checkArguments(args);

        // Load language file
        try {
            lang = new Lang(config.lang);
        } catch (IOException ex) {
            logger.severe("Failed to load language file: " + config.lang + "!");
            ex.printStackTrace();
            return;
        }
        // Load some more stuff
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, Versioning.MINECRAFT_PC_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, Versioning.MINECRAFT_PE_VERSION));
        authMode = config.mode.toLowerCase();
        if (!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline")) {
            logger.severe("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode
                    + "'! ");
            return;
        }

        // Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.thread_pool_size));
        generalThreadPool = Executors.newScheduledThreadPool(config.thread_pool_size);

        // Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.udp_bind_ip, config.udp_bind_port));
        // RakNet.enableLogging();
        network = new RaknetInterface(this, config.udp_bind_ip, // IP
                config.udp_bind_port); // Port

        // MOTD
        motd = config.motd;
        motd = motd.replace("&", "\u00a7");

        network.setBroadcastName(motd, 1, 2);
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public void onTick() {
        // network.onTick();
        sessionRegister.onTick();
    }

    public void checkArguments(String[] args) {
        for (String arg : args) {
            if (arg.toLowerCase().contains("--debug")) {
                debug = true;
                getLogger().debug = true;
                logger.info(MCColor.DARK_AQUA + "Proxy is running in debug mode.");
            }
        }
    }

    public void shutdown() {
        logger.info(lang.get(Lang.SHUTTING_DOWN));

        debug = false;
        this.shuttingDown = true;
        network.shutdown();
        try {
            Thread.sleep(2000); // Wait for all clients disconnected
        } catch (Exception ex) {
            System.out.println("Exception while shutting down!");
            ex.printStackTrace();
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
