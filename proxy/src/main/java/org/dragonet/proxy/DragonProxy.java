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
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.dragonet.common.data.blocks.Block;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.proxy.commands.CommandRegister;
import org.dragonet.proxy.commands.ConsoleCommandReader;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.utilities.Logger;
import org.dragonet.proxy.utilities.MetricsManager;
import org.yaml.snakeyaml.Yaml;

import com.github.steveice10.mc.protocol.MinecraftConstants;

import co.aikar.timings.Timings;

public class DragonProxy {

    public static final boolean IS_RELEASE = false; // DO NOT CHANGE, ONLY ON PRODUCTION

    private static DragonProxy instance;
    private static String[] launchArgs;
    private final Properties properties;
    private String version;
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
        launchArgs = args;
        getInstance();
    }

    public static DragonProxy getInstance() {
        if (instance == null)
            instance = new DragonProxy();
        return instance;
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

    private DragonProxy() {
        logger = new Logger(this);

        try {
            File fileConfig = new File("config.yml");
            if (!fileConfig.exists()) {
                // Create default config
                FileOutputStream fos = new FileOutputStream(fileConfig);
                InputStream ins = DragonProxy.class.getResourceAsStream("/config.yml");
                int data;
                while ((data = ins.read()) != -1)
                    fos.write(data);
                ins.close();
                fos.close();
            }
            config = new Yaml().loadAs(new FileInputStream(fileConfig), ServerConfig.class);
        } catch (IOException ex) {
            logger.severe("Failed to load configuration file! Make sure the file is writable.");
            ex.printStackTrace();
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/buildNumber.properties");
        properties = new Properties();
        
                if (inputStream != null) {
                    try {
                        properties.load(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read properties file", e);
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
        // Initialize console command reader
        console = new ConsoleCommandReader(this);
        console.startConsole();

        // Should we save console log? Set it in config file
        if (config.log_console)
//            console.startFile("console.log"); TODO
            logger.info("Saving console output enabled");
        else
            logger.info("Saving console output disabled");
        
        // set logger mode
        logger.debug = config.log_debug;
        
        // set logger colors mod
        logger.colorful = config.log_colors;

        // Put at the top instead
        if (!IS_RELEASE)
            logger.warning("This is a development build. It may contain bugs. Do not use on production.\n");

        // Check for startup arguments
        checkArguments(launchArgs);

        // Load language file
        try {
            lang = new Lang(config.lang);
        } catch (IOException ex) {
            logger.severe("Failed to load language file: " + config.lang + "!");
            ex.printStackTrace();
        }

        // Load some more stuff
        version = properties.getProperty("git.build.version");
        if (properties.containsKey("git.commit.id.describe"))
            version += " (" + properties.getProperty("git.commit.id.describe") + ")";
        logger.info(lang.get(Lang.INIT_LOADING, version));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, MinecraftConstants.GAME_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, ProtocolInfo.MINECRAFT_VERSION));
        authMode = config.mode.toLowerCase();
        if (!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline"))
            logger.severe("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode
                + "'! ");

        // Init metrics (https://bstats.org/plugin/server-implementation/DragonProxy)
        MetricsManager.getInstance();
        
        // Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);

        // Init block handling
        Block.init();

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.thread_pool_size));
        generalThreadPool = Executors.newScheduledThreadPool(config.thread_pool_size);
        
        // MOTD
        motd = config.motd;
        motd = motd.replace("&", "\u00a7");

        // Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.udp_bind_ip, config.udp_bind_port));
        // RakNet.enableLogging();
        network = new RaknetInterface(this, config.udp_bind_ip, // IP
            config.udp_bind_port, // Port
            motd, config.max_players);

        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public Properties getProperties() {
        return this.properties;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMotd() {
        return this.motd;
    }

    public void onTick() {
        sessionRegister.onTick();
    }

    public void checkArguments(String[] args) {
        if (args != null) {
            for (String arg : args) {
                if (arg.toLowerCase().contains("--debug")) {
                    debug = true;
                    getLogger().debug = true;
                    logger.info("Proxy is running in debug mode.");
                }
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
        Timings.stopServer();
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
