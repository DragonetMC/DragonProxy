package org.dragonet.dragonproxy.proxy;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import org.dragonet.dragonproxy.api.Proxy;
import org.dragonet.dragonproxy.proxy.configuration.ConfigurationProvider;
import org.dragonet.dragonproxy.proxy.configuration.DragonConfiguration;
import org.dragonet.dragonproxy.proxy.locale.DragonLocale;
import org.dragonet.dragonproxy.proxy.locale.LocaleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DragonProxy implements Proxy {

    private Logger logger;
    private Injector injector;
    private DragonConfiguration configuration;
    private DragonLocale locale;

    public DragonProxy(int bedrockPort, int javaPort) {
        // Initialize the logger
        logger = LoggerFactory.getLogger("DragonProxy");
        logger.info("Welcome to DragonProxy version " + getVersion());
        // Initialize services
        try {
            initialize();
        } catch (Throwable th) {
            logger.error("A fatal error occurred while initializing the proxy!", th);
            System.exit(-1);
            return;
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

        // Load configuration
        configuration = injector.getSingleton(DragonConfiguration.class);
        locale = injector.getSingleton(DragonLocale.class);

        // Initiate services

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
    public void shutdown() {
    }
}
