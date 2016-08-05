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
import java.util.logging.Logger;

import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.proxy.utilities.Terminal;
import org.dragonet.proxy.commands.CommandRegister;

import org.mcstats.Metrics;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

public class DragonProxy {

    public static void main(String[] args) {
        new DragonProxy().run(args);
    }
	
    public final static boolean IS_RELEASE = false; //DO NOT CHANGE, ONLY ON PRODUCTION

    @Getter
    private final Logger logger = Logger.getLogger("DragonProxy");

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

    private ConsoleManager console;

    private Metrics metrics;

    private String motd;

    private boolean isDebug = false;

    public void run(String[] args) {
        //Need to initialize config before console
        try {
            File fileConfig = new File("config.yml");
            if (!fileConfig.exists()) {
                //Create default config
                FileOutputStream fos = new FileOutputStream(fileConfig);
                InputStream ins = DragonProxy.class.getResourceAsStream("/config.yml");
                int data = -1;
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

        //Initialize console
        console = new ConsoleManager(this);
        console.startConsole();

        logger.info(Terminal.GREEN + "\n\n              DRAGONPROXY MULTI MAVEN MODULE TEST\n   " +
                "This is not ready for production yet and may not ever be ;)\n");

	    //Put at the top instead
	    if(!IS_RELEASE) {
	    	logger.warning(Terminal.YELLOW + "This is a development build. It may contain bugs. Do not use on production.\n");
	    }

	    //Check for startup arguments
        checkArguments(args);

    	//Should we save console log? Set it in config file
        if(config.isLog_console()){
            console.startFile("console.log");
            logger.info("Saving console output enabled"); //TODO: Translations
        } else {
            logger.info("Saving console output disabled");
        }
		
	    //Load language file
        try {
            lang = new Lang(config.getLang());
        } catch (IOException ex) {
            logger.severe("Failed to load language file: " + config.getLang() + "!");
            ex.printStackTrace();
            return;
        }
	    //Load some more stuff
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, Versioning.MINECRAFT_PC_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, Versioning.MINECRAFT_PE_VERSION));
        authMode = config.getMode().toLowerCase();
        if(!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline")){
            logger.severe("Invalid login 'mode' option detected, must be cls/online/offline. You set it to '" + authMode + "'! ");
            return;
        }
		
	    //Init session and command stuff
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);
		
        if (IS_RELEASE) {
            try {
                metrics = new ServerMetrics(this);
                metrics.start();
            } catch (IOException ex) { }
        }

        //Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.getThread_pool_size()));
        generalThreadPool = Executors.newScheduledThreadPool(config.getThread_pool_size());

        //Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getUdp_bind_ip(), config.getUdp_bind_port()));
        network = new RaknetInterface(this,
                config.getUdp_bind_ip(), //IP
                config.getUdp_bind_port()); //Port

        //MOTD
        motd = config.getMotd();
        motd = motd.replace("&", "§");

        network.setBroadcastName(motd, -1, -1);
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public boolean isDebug(){
        return isDebug;
    }

    public void onTick() {
        network.onTick();
        sessionRegister.onTick();
    }

    public void checkArguments(String[] args){
        for(String arg : args){
            if(arg.toLowerCase().contains("--debug")){
                isDebug = true;
                logger.info(Terminal.CYAN + "Proxy is running in debug mode.");
            }
        }
    }

    public void shutdown() {
        logger.info(lang.get(Lang.SHUTTING_DOWN));

        isDebug = false;
        this.shuttingDown = true;
        network.shutdown();
        try{
            Thread.sleep(2000); //Wait for all clients disconnected
        } catch (Exception e) {
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
