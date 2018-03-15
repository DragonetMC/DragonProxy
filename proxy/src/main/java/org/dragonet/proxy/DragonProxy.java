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
import org.dragonet.proxy.events.EventManager;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.utilities.ProxyLogger;
import org.dragonet.proxy.utilities.MetricsManager;
import org.dragonet.proxy.utilities.PluginManager;
import org.dragonet.proxy.utilities.pingpassthrough.PingThread;
import org.yaml.snakeyaml.Yaml;

import com.github.steveice10.mc.protocol.MinecraftConstants;

import co.aikar.timings.Timings;
import org.apache.commons.lang3.SystemUtils;
import org.dragonet.common.utilities.SkinFetcher;

public class DragonProxy {

    public static final boolean IS_RELEASE = false; // TODO: remove

    private static DragonProxy instance;
    private static String[] launchArgs;
    private final Properties properties;
    private String version;
    private ProxyLogger logger;
    private final TickerThread ticker = new TickerThread(this);
    private ServerConfig config;
    private Lang lang;
    private SessionRegister sessionRegister;
    private RaknetInterface network;
    private boolean shuttingDown;
    private ScheduledExecutorService generalThreadPool;
    private CommandRegister commandRegister;
    private SkinFetcher skinFetcher;
    private String authMode;
    private ConsoleCommandReader console;
    private String motd;
    private boolean debug = false;
    private PluginManager pluginManager;
    private EventManager eventManager;

    public static void main(String[] args) {
        launchArgs = args;
        getInstance();
    }

    public static DragonProxy getInstance() {
        if (instance == null)
            instance = new DragonProxy();
        return instance;
    }

    public ProxyLogger getLogger() {
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

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public EventManager getEventManager() {
        return eventManager;
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
        instance = this;
        logger = new ProxyLogger(this);

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
            logger.info("Failed to load configuration file! Make sure the file is writable.");
            ex.printStackTrace();
            System.exit(1);
        } catch (org.yaml.snakeyaml.error.YAMLException ex) {
            logger.info("Failed to load configuration file! Make sure it's up to date !");
            System.exit(1);
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/buildNumber.properties");
        properties = new Properties();

        if (inputStream != null)
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
        // Initialize console command reader
        console = new ConsoleCommandReader(this);
        console.startConsole();

        // set logger mode
        logger.debug = config.log_debug;

        // set logger colors mod
        logger.colorful = config.log_colors;

        // Put at the top instead
        if (!IS_RELEASE)
            logger.info("This is a development build. It may contain bugs. Do not use on production.");

        if (config.auto_login) {
            logger.info("******************************************");
            logger.info("");
            logger.info("\tYou're using autologin, make sure you are the only who can connect on this server !");
            logger.info("");
            logger.info("******************************************");
        }

        // Check for startup arguments
        checkArguments(launchArgs);

        // Load language file
        try {
            lang = new Lang(config.lang);
        } catch (IOException ex) {
            logger.info("Failed to load language file: " + config.lang + "!");
            ex.printStackTrace();
        }

        // Load some more stuff
        version = properties.getProperty("git.build.version");
        if (properties.containsKey("git.commit.id.describe"))
            version += " (" + properties.getProperty("git.commit.id.describe") + ")";
        logger.info(lang.get(Lang.INIT_LOADING, version));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, MinecraftConstants.GAME_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, ProtocolInfo.MINECRAFT_VERSION));
        logger.info("Java version : " + SystemUtils.JAVA_VERSION);
        logger.info("System arch : " + SystemUtils.OS_ARCH);
        logger.info("System os : " + SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);

        authMode = config.mode.toLowerCase();
        if (!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline") && !authMode.equals("hybrid"))
            logger.info("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode
                    + "'! ");

        // Init metrics (https://bstats.org/plugin/server-implementation/DragonProxy)
        new MetricsManager(this);

        // Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);
        skinFetcher = new SkinFetcher();

        // Init block handling
        Block.init();

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.thread_pool_size));
        generalThreadPool = Executors.newScheduledThreadPool(config.thread_pool_size);

        // MOTD
        motd = config.motd;
        motd = motd.replace("&", "\u00a7");

        File pluginfolder = new File("plugins");
        pluginfolder.mkdirs();

        // create the plugin manager
        pluginManager = new PluginManager(pluginfolder.toPath());
        eventManager = new EventManager(this);

        // start and load all plugins of application
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        // Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.udp_bind_ip, config.udp_bind_port));
        // RakNet.enableLogging();
        network = new RaknetInterface(this, config.udp_bind_ip, // IP
                config.udp_bind_port, // Port
                motd, config.auto_login ? 1 : config.max_players);

        if (DragonProxy.getInstance().getConfig().ping_passthrough)
            new PingThread();

        ticker.start();

        logger.info(lang.get(Lang.INIT_DONE));

//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                shutdown();
//            }
//        });
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
        if (args != null)
            for (String arg : args)
                if (arg.toLowerCase().contains("--debug")) {
                    debug = true;
                    getLogger().debug = true;
                    logger.info("Proxy is running in debug mode.");
                }
    }

    public void shutdown() {
        logger.info(lang.get(Lang.SHUTTING_DOWN));

        pluginManager.stopPlugins();
        
        debug = false;
        this.shuttingDown = true;
        network.shutdown();
        try {
            Thread.sleep(2000); // Wait for all clients disconnected
        } catch (Exception ex) {
            logger.info("Exception while shutting down!");
            ex.printStackTrace();
        }
        Timings.stopServer();
        System.out.println("Goodbye!");
        logger.stop();
        System.exit(0);
    }

    /**
     * @return the skinFetcher
     */
    public SkinFetcher getSkinFetcher() {
        return skinFetcher;
    }
}
