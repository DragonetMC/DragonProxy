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
import org.apache.logging.log4j.LogManager;
import org.dragonet.dragonproxy.api.Proxy;
import org.dragonet.dragonproxy.proxy.configuration.ConfigurationProvider;
import org.dragonet.dragonproxy.proxy.configuration.DragonConfiguration;
import org.dragonet.dragonproxy.proxy.console.DragonConsole;
import org.dragonet.dragonproxy.proxy.locale.DragonLocale;
import org.dragonet.dragonproxy.proxy.locale.LocaleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DragonProxy implements Proxy {

    private static DragonProxy instance;

    private Logger logger;
    private Injector injector;
    private DragonConsole console;
    private DragonConfiguration configuration;
    private DragonLocale locale;

    private AtomicBoolean shutdownInProgress = new AtomicBoolean(false);
    private boolean shutdown = false;

    public final int bedrockPort;
    public final int javaPort;

    public DragonProxy(int bedrockPort, int javaPort) {
        this.bedrockPort = bedrockPort;
        this.javaPort = javaPort;
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
        instance = this;
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

    public static DragonProxy getInstance() {
        return instance;
    }
}
