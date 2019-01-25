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
package org.dragonet.dragonproxy.proxy;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.dragonet.dragonproxy.api.Proxy;
import org.dragonet.dragonproxy.proxy.configuration.ConfigurationProvider;
import org.dragonet.dragonproxy.proxy.configuration.DragonConfiguration;
import org.dragonet.dragonproxy.proxy.console.DragonConsole;
import org.dragonet.dragonproxy.proxy.locale.DragonLocale;
import org.dragonet.dragonproxy.proxy.locale.LocaleProvider;
import org.dragonet.dragonproxy.proxy.server.ServerInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonProxy implements Proxy {

    private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    private Logger logger;
    private Injector injector;
    private DragonConsole console;
    private DragonConfiguration configuration;
    private DragonLocale locale;
    private static DragonProxy instance = null;

    private AtomicBoolean shutdownInProgress = new AtomicBoolean(false);
    private boolean shutdown = false;

    DragonProxy(int bedrockPort, int javaPort) {
        if(instance == null){
            instance = this;
        }
        else{
            throw new ExceptionInInitializerError();
        }
        //Set properties
        properties.put("bedrockPort", Integer.toString(bedrockPort));
        properties.put("javaPort", Integer.toString(javaPort));
        // Initialize the logger
        logger = LoggerFactory.getLogger(DragonProxy.class);
        logger.info("Welcome to DragonProxy version " + getVersion());
        // Initialize services
        try {
            initialize();
        } catch (Throwable th) {
            logger.error("A fatal error occurred while initializing the proxy!", th);
            LogManager.shutdown();
            System.exit(1);
        }
    }

    private void initialize() {
        // Create injector, provide elements from the environment and register providers
        injector = new InjectorBuilder()
            .addDefaultHandlers("org.dragonet.dragonproxy")
            .create();
        injector.register(DragonProxy.class, this);
        injector.register(Logger.class, logger);
        injector.provide(ProxyFolder.class, getFolder());
        injector.registerProvider(DragonConfiguration.class, ConfigurationProvider.class);
        injector.registerProvider(DragonLocale.class, LocaleProvider.class);

        // Initiate console
        console = injector.getSingleton(DragonConsole.class);

        // Load configuration
        configuration = injector.getSingleton(DragonConfiguration.class);
        locale = injector.getSingleton(DragonLocale.class);

        // Initiate services

    }

    public boolean isShutdown() {
        return shutdown;
    }

    void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ServerInit());
        bootstrap.bind(Integer.parseInt(getProperty("javaPort")));
    }

    @Override
    public void shutdown() {
        if (!shutdownInProgress.compareAndSet(false, true)) {
            return;
        }
        logger.info("Shutting down the proxy...");

        // TODO: shutdown

        shutdown = true;
    }

    @Override
    public DragonConsole getConsole() {
        return console;
    }

    @Override
    public String getVersion() {
        return DragonProxy.class.getPackage().getImplementationVersion();
    }

    @Override
    public Path getFolder() {
        return Paths.get("");
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key) != null ?properties.get(key) :"null";
    }

    @Override
    public void setProperty(String key, String value) {
        if(properties.get(key) != null) {
            properties.replace(key, value);
        }
        else {
            properties.put(key, value);
        }
    }

    public static DragonProxy getInstance() {
        return instance;
    }
}
