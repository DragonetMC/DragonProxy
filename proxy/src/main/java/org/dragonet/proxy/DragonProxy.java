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

import org.mcstats.Metrics;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

public class DragonProxy {

    public static void main(String[] args) {
        new DragonProxy().run(args);
    }
	
    public final static boolean IS_RELEASE = false; //DO NOT CHANGE, ONLY ON PRODUCTION

    @Getter
    private Logger logger;

    private final TickerThread ticker = new TickerThread(this);

    @Getter
    private ServerConfig config;

    @Getter
    private Lang lang;

    @Getter
    private SessionRegister sessionRegister;

    @Getter
    private RaknetInterface network;

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

    private String motd;

    @Getter
    private boolean isDebug = false;

    public void run(String[] args) {
        logger = new Logger(this);

        try {
            File fileConfig = new File("config.yml");
            if (!fileConfig.exists()) {
                //Create default config
                FileOutputStream fos = new FileOutputStream(fileConfig);
                InputStream ins = DragonProxy.class.getResourceAsStream("/config.yml");
                int data;
                while((data = ins.read()) != -1){
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
       /* if(config.isLog_console()){
            console.startFile("console.log");
            logger.info("Saving console output enabled");
        } else {
            logger.info("Saving console output disabled");
        } */

        // Put at the top instead
        if(!IS_RELEASE) {
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
        if(!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline")){
            logger.severe("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode + "'! ");
            return;
        }
		
	    // Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);

        // Start metrics
        try {
            metrics = new ServerMetrics(this);
            metrics.start();
            logger.debug("Started metrics succesfully.");
        } catch (IOException ex) {
            logger.warning("Failed to start metrics: " + ex);
        }

        // Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.getThread_pool_size()));
        generalThreadPool = Executors.newScheduledThreadPool(config.getThread_pool_size());

        // Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getUdp_bind_ip(), config.getUdp_bind_port()));
        network = new RaknetInterface(this,
                config.getUdp_bind_ip(), //IP
                config.getUdp_bind_port()); //Port

        // MOTD
        motd = config.getMotd();
        motd = motd.replace("&", "§");

        network.setBroadcastName(motd, -1, -1);
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public void onTick() {
        // network.onTick();
        sessionRegister.onTick();
    }

    public void checkArguments(String[] args){
        for(String arg : args){
            if(arg.toLowerCase().contains("--debug")){
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
        network.shutdown();
        try{
            Thread.sleep(2000); // Wait for all clients disconnected
        } catch (Exception ex) {
            System.out.println("Exception while shutting down!");
            ex.printStackTrace();
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
