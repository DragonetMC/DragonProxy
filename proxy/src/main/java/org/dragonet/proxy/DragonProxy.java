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
import org.dragonet.common.data.blocks.GlobalBlockPalette;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.proxy.commands.CommandRegister;
import org.dragonet.proxy.commands.ConsoleCommandReader;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.events.EventManager;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.translator.SoundTranslator;
import org.dragonet.proxy.utilities.ProxyLogger;
import org.dragonet.proxy.utilities.MetricsManager;
import org.dragonet.proxy.utilities.PluginManager;
import org.dragonet.proxy.utilities.pingpassthrough.PingThread;
import org.yaml.snakeyaml.Yaml;

import com.github.steveice10.mc.protocol.MinecraftConstants;

import co.aikar.timings.Timings;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.SystemUtils;
import org.dragonet.proxy.utilities.SkinFetcher;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.commands.ICommandRegister;
import org.dragonet.api.commands.IConsoleCommandReader;
import org.dragonet.api.configuration.IServerConfig;
import org.dragonet.api.events.IEventManager;
import org.dragonet.api.language.ILang;
import org.dragonet.api.logger.IProxyLogger;
import org.dragonet.api.plugin.IPluginManager;
import org.dragonet.api.sessions.IRaknetInterface;
import org.dragonet.api.sessions.ISessionRegister;
import org.dragonet.api.skins.ISkinFetcher;
import org.dragonet.api.sound.ISoundTranslator;
import org.pf4j.AbstractPluginManager;

public class DragonProxy implements ProxyServer {

    public static final boolean IS_RELEASE = false; // TODO: remove

    private static ProxyServer instance;
    private static String[] launchArgs;
    private final Properties properties;
    private boolean isContainer = false;
    private String version;
    private IProxyLogger logger;
    private final TickerThread ticker = new TickerThread(this);
    private IServerConfig config;
    private ILang lang;
    private final ISessionRegister sessionRegister;
    private final IRaknetInterface network;
    private boolean shuttingDown;
    private final ScheduledExecutorService generalThreadPool;
    private final ICommandRegister commandRegister;
    private final ISkinFetcher skinFetcher;
    private final String authMode;
    private IConsoleCommandReader console;
    private String motd;
    private boolean debug = false;
    private final AbstractPluginManager pluginManager;
    private final IEventManager eventManager;
    private final ISoundTranslator soundTranslator;

    public static void main(String[] args) {
        launchArgs = args;
        getInstance();
    }

    public static ProxyServer getInstance() {
        if (instance == null)
            instance = new DragonProxy();
        return instance;
    }

    @Override
    public IProxyLogger getLogger() {
        return logger;
    }

    @Override
    public IServerConfig getConfig() {
        return config;
    }

    @Override
    public ILang getLang() {
        return lang;
    }

    @Override
    public ISessionRegister getSessionRegister() {
        return sessionRegister;
    }

    @Override
    public IRaknetInterface getNetwork() {
        return network;
    }

    @Override
    public AbstractPluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public IEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public ISoundTranslator getSoundTranslator() {
        return soundTranslator;
    }

    @Override
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    @Override
    public ScheduledExecutorService getGeneralThreadPool() {
        return generalThreadPool;
    }

    @Override
    public ICommandRegister getCommandRegister() {
        return commandRegister;
    }

    @Override
    public String getAuthMode() {
        return authMode;
    }

    @Override
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

        // Load language file
        try {
            lang = new Lang(config.getLang());
        } catch (IOException ex) {
            logger.info("Failed to load language file: " + config.getLang() + "!");
            ex.printStackTrace();
        }

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.getThread_pool_size()));
        generalThreadPool = Executors.newScheduledThreadPool(config.getThread_pool_size());

        // Initialize console command reader
        console = new ConsoleCommandReader(this);
        console.startConsole();

        // set logger colors mod
        logger.setColorFull(config.isLog_colors());

        // Put at the top instead
        if (!IS_RELEASE)
            logger.info("This is a development build. It may contain bugs. Do not use on production.");

        if (config.getAuto_login()) {
            logger.info("******************************************");
            logger.info("");
            logger.info("\tYou're using autologin, make sure you are the only who can connect on this server !");
            logger.info("");
            logger.info("******************************************");
        }

        // Check for startup arguments
        checkArguments(launchArgs);

        if (config.isLog_debug() && !debug) {
            logger.setDebug(true);
            debug = true;
            logger.info("Proxy running in debug mode.");
        }

        // Load some more stuff
        version = properties.getProperty("git.build.version");
        if (properties.containsKey("git.commit.id.describe"))
            version += " (" + properties.getProperty("git.commit.id.describe") + ")";

        // Check profile, used for docker profile
        if (System.getProperties().containsKey("org.dragonet.proxy.profile"))
            if (System.getProperties().get("org.dragonet.proxy.profile").equals("container")) {
                isContainer = true;
                version += "-docker";
            }

        logger.info(lang.get(Lang.INIT_LOADING, version));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, MinecraftConstants.GAME_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, ProtocolInfo.MINECRAFT_VERSION));
        logger.info("Java version : " + SystemUtils.JAVA_VERSION);
        logger.info("System arch : " + SystemUtils.OS_ARCH);
        logger.info("System os : " + SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);

        authMode = config.getMode().toLowerCase();
        if (!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline") && !authMode.equals("hybrid"))
            logger.info("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode
                    + "'! ");

        // Init metrics (https://bstats.org/plugin/server-implementation/DragonProxy)
        new MetricsManager(this);

        // Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);
        skinFetcher = new SkinFetcher();

        GlobalBlockPalette.getOrCreateRuntimeId(0, 0); // Force it to load

        // Init block handling
        Block.init();

        // MOTD
        motd = config.getMotd();
        motd = motd.replace("&", "\u00a7");

        File pluginfolder = new File("plugins");
        pluginfolder.mkdirs();

        // create the plugin manager
        pluginManager = new PluginManager(pluginfolder.toPath());
        eventManager = new EventManager(this);

        soundTranslator = new SoundTranslator();

        // start and load all plugins of application
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        // Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getUdp_bind_ip(), config.getUdp_bind_port()));
        // RakNet.enableLogging();
        network = new RaknetInterface(this, config.getUdp_bind_ip(), // IP
                config.getUdp_bind_port(), // Port
                motd, config.getAuto_login() ? 1 : config.getMax_players());

        if (DragonProxy.getInstance().getConfig().isPing_passthrough())
            generalThreadPool.scheduleAtFixedRate(new PingThread(), 1, 1, TimeUnit.SECONDS);

        generalThreadPool.execute(ticker);

        logger.info(lang.get(Lang.INIT_DONE));

//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                this.setName("ShutdownThread");
//                if (!isShuttingDown())
//                    shutdown();
//            }
//        });
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getMotd() {
        return this.motd;
    }

    @Override
    public void onTick() {
        sessionRegister.onTick();
    }

    @Override
    public void checkArguments(String[] args) {
        if (args != null)
            for (String arg : args)
                if (arg.toLowerCase().contains("--debug")) {
                    debug = true;
                    getLogger().setDebug(true);
                    logger.info("Proxy is running in debug mode.");
                }
    }

    @Override
    public void shutdown() {
        this.logger.info(lang.get(Lang.SHUTTING_DOWN));

        this.pluginManager.stopPlugins();

        this.debug = false;
        this.shuttingDown = true;
        this.network.shutdown();
//        try {
//            Thread.sleep(2000); // Wait for all clients disconnected
//        } catch (Exception ex) {
//            logger.info("Exception while shutting down!");
//            ex.printStackTrace();
//        }
        this.logger.stop();
        this.generalThreadPool.shutdown();
        System.out.println("Goodbye!");
        System.exit(0);
    }

    /**
     * @return the skinFetcher
     */
    @Override
    public ISkinFetcher getSkinFetcher() {
        return skinFetcher;
    }
}
