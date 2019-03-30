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
package org.dragonet.proxy;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import com.nukkitx.network.raknet.RakNetServer;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import com.nukkitx.protocol.bedrock.wrapper.WrappedPacket;
import org.apache.logging.log4j.LogManager;
import org.dragonet.dragonproxy.api.Proxy;
import org.dragonet.proxy.configuration.ConfigurationProvider;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.console.DragonConsole;
import org.dragonet.proxy.locale.DragonLocale;
import org.dragonet.proxy.locale.LocaleProvider;
import org.dragonet.proxy.network.ProxyRakNetEventListener;
import org.dragonet.proxy.network.UpstreamPacketHandler;
import org.dragonet.proxy.network.session.ProxyBedrockSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonProxy implements Proxy {

    private static final boolean RELEASE = false;

    private Logger logger;
    private Injector injector;
    private DragonConsole console;
    private DragonConfiguration configuration;
    private DragonLocale locale;
    private RakNetServer raknetServer;

    private AtomicBoolean shutdownInProgress = new AtomicBoolean(false);
    private boolean shutdown = false;

    public DragonProxy(int bedrockPort, int javaPort) {
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
            return;
        }
    }

    private void initialize() {
        if(!RELEASE) {
            logger.warn("This is a development build. It may contain bugs. Do not use on production.");
        }

        // Create injector, provide elements from the environment and register providers
        injector = new InjectorBuilder()
            .addDefaultHandlers("org.dragonet.proxy")
            .create();
        injector.register(DragonProxy.class, this);
        injector.register(Logger.class, logger);
        injector.provide(ProxyFolder.class, getFolder());
        injector.registerProvider(DragonConfiguration.class, ConfigurationProvider.class); // TODO: migrate to jackson
        injector.registerProvider(DragonLocale.class, LocaleProvider.class);

        // Initiate console
        console = injector.getSingleton(DragonConsole.class);

        // Load configuration
        configuration = injector.getSingleton(DragonConfiguration.class);
        locale = injector.getSingleton(DragonLocale.class);

        // Initiate RakNet
        RakNetServer.Builder<BedrockSession<ProxyBedrockSession>> builder = RakNetServer.builder();
        builder.eventListener(new ProxyRakNetEventListener())
            .address(new InetSocketAddress("0.0.0.0", 19132))
            .packet(WrappedPacket::new, 0xfe)
            .sessionManager(ProxyBedrockSession.MANAGER)
            .sessionFactory(rakNetSession -> {
                BedrockSession<ProxyBedrockSession> session = new BedrockSession<>(rakNetSession);
                session.setHandler(new UpstreamPacketHandler(session, this));
                return session;
            });
        raknetServer = builder.build();
        if (raknetServer.bind()) {
            logger.info("RakNet server started on {}", "0.0.0.0");
        } else {
            logger.error("RakNet server failed to bind to {}", "0.0.0.0");
        }

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
}
