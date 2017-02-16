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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.Getter;

import org.dragonet.proxy.commands.CommandRegister;
import org.dragonet.proxy.commands.ConsoleCommandReader;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.network.adapter.ClientProtocolAdapter;
import org.dragonet.proxy.network.adapter.MCPCClientProtocolAdapter;
import org.dragonet.proxy.network.adapter.MCPEClientProtocolAdapter;
import org.dragonet.proxy.utilities.Logger;
import org.dragonet.proxy.utilities.MCColor;
import org.dragonet.proxy.utilities.Versioning;
import org.mcstats.Metrics;
import org.yaml.snakeyaml.Yaml;

//TODO: DragonProxy should not manage Raknet anymore. Move to ProtocolAdapter
public class DragonProxy {

    @Getter
    private static DragonProxy self = new DragonProxy();

    public static void main(String[] args) {
        self.run(args);
    }

    public final static boolean IS_RELEASE = false; //DO NOT CHANGE, ONLY ON PRODUCTION

    private static Logger logger = null;

    private final TickerThread ticker = new TickerThread(this);

    @Getter
    private ServerConfig config;

    @Getter
    private Lang lang;

    @Getter
    private NetworkConnectionManager network;

    @Getter
    private boolean shuttingDown;

    @Getter
    private ScheduledExecutorService generalThreadPool;

    @Getter
    private CommandRegister commandRegister;

    @Getter
    private String authMode;

    private ConsoleCommandReader console;

    private Metrics metrics;

    @Getter
    private boolean isDebug = false;

    public void run(String[] args) {
        logger = new Logger(this, new File("proxy.log"));
        // Load config.yml 

        try {
            File fileConfig = new File("config.yml");

            boolean newConfig = false;
            if (!fileConfig.exists()) {
                newConfig = fileConfig.createNewFile();
                getLogger().info("Generating blank config file");
            }
            config = new Yaml().loadAs(new FileInputStream(fileConfig), ServerConfig.class);

            if (config == null) {
                config = new ServerConfig();
                getLogger().warning("Config file was null. Continuing by using default values");
            }

            if (newConfig) {
                getLogger().info("Writing default values to blank config file");
                Map<String, RemoteServer> servers = new HashMap<>();
                DesktopServer serv = new DesktopServer();
                serv.setRemote_addr("127.0.0.1");
                serv.setRemote_port(25565);
                servers.put("localhost", serv);
                config.setRemote_servers(servers);
                config.setDefault_server("localhost");
                config.setAcceptPCClients(false);
                config.setMax_players(10);
                config.setMode("offline");
                String str = new Yaml().dump(config);
                FileOutputStream fos = new FileOutputStream(fileConfig);

                for (byte bytes : str.getBytes()) {
                    fos.write(bytes);
                }
                fos.flush();
                fos.close();
            }
        } catch (IOException ex) {
            logger.severe("Failed to load configuration file! Make sure the file is writable.");
            ex.printStackTrace();
            return;
        }

        // Initialize console command reader
        console = new ConsoleCommandReader(this);
        console.startConsole();

        // Should we save console log? Set it in config file
        /* if(config.isLog_console()){
            console.startFile("console.log");
            logger.info("Saving console output enabled");
        } else {
            logger.info("Saving console output disabled");
        } */
        // Put at the top instead
        if (!IS_RELEASE) {
            logger.warning(MCColor.YELLOW + "This is a development build. It may contain bugs. Do not use on production.\n");
        }

        // Check for startup arguments
        checkArguments(args);

        // Load language file
        try {
            lang = new Lang(config.getLang());
        } catch (IOException ex) {
            logger.severe("Failed to load language file: " + config.getLang() + "!");
            ex.printStackTrace();
            return;
        }
        // Load some more stuff
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, Versioning.MINECRAFT_PC_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, Versioning.MINECRAFT_PE_VERSION));
        authMode = config.getMode().toLowerCase();
        if (!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline")) {
            logger.severe("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode + "'! ");
            return;
        }

        // Init command stuff
        commandRegister = new CommandRegister(this);

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.getThread_pool_size()));
        generalThreadPool = Executors.newScheduledThreadPool(config.getThread_pool_size());

        // Bind
        ClientProtocolAdapter adapter = (getConfig().isAcceptPCClients() ? new MCPCClientProtocolAdapter() : new MCPEClientProtocolAdapter());
        network = new NetworkConnectionManager(this, adapter);

        // MOTD
        network.setMotd(config.getMotd().replace("&", "§"));

        // Start metrics
        try {
            metrics = new ServerMetrics(this);
            metrics.start();
            logger.debug("Started metrics succesfully.");
        } catch (IOException ex) {
            logger.warning("Failed to start metrics: " + ex);
        }

        logger.info(lang.get(Lang.INIT_BINDING, config.getUdp_bind_ip(), config.getUdp_bind_port()));

        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public static Logger getLogger() {
        // TODO: In the future this should never return null
        return logger;
    }

    public String getMotd() {
        return network.getMotd();
    }

    public void onTick() {
        network.onTick();
    }

    public void checkArguments(String[] args) {
        for (String arg : args) {
            if (arg.toLowerCase().contains("--debug")) {
                isDebug = true;
                getLogger().debug = true;
                logger.info(MCColor.DARK_AQUA + "Proxy is running in debug mode.");
            }
        }
    }

    public void shutdown() {
        logger.info(lang.get(Lang.SHUTTING_DOWN));

        isDebug = false;
        this.shuttingDown = true;
        //network.shutdown();
        try {
            Thread.sleep(2000); // Wait for all clients disconnected
        } catch (Exception ex) {
            DragonProxy.getLogger().severe("Exception while shutting down!");
            ex.printStackTrace();
        }
        DragonProxy.getLogger().info("Goodbye!");
        System.exit(0);
    }
}
