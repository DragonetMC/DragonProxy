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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.nukkitx.network.raknet.RakNetServer;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import com.nukkitx.protocol.bedrock.wrapper.WrappedPacket;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;

import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.console.DragonConsole;
import org.dragonet.proxy.network.ProxyRakNetEventListener;
import org.dragonet.proxy.network.ProxySessionManager;
import org.dragonet.proxy.network.UpstreamPacketHandler;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.PacketTranslatorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonProxy {

    private static final boolean RELEASE = false;
    public static DragonProxy INSTANCE = null;

    private Logger logger;
    private Injector injector;
    @Getter
    private DragonConsole console;
    @Getter
    private DragonConfiguration configuration;
    private RakNetServer raknetServer;
    private ProxySessionManager sessionManager;

    private AtomicBoolean shutdownInProgress = new AtomicBoolean(false);
    private boolean shutdown = false;

    public DragonProxy(int bedrockPort, int javaPort) {
        INSTANCE = this;
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

    private void initialize() throws Exception {
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

        // Initiate console
        console = injector.getSingleton(DragonConsole.class);

        // Load configuration
        // TODO: Tidy this up
        File fileConfig = new File("configuration.yml");
        if (!fileConfig.exists()) {
            // Create default config
            FileOutputStream fos = new FileOutputStream(fileConfig);
            InputStream ins = DragonProxy.class.getResourceAsStream("/configuration.yml");
            int data;
            while ((data = ins.read()) != -1) {
                fos.write(data);
            }
            ins.close();
            fos.close();
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        configuration = mapper.readValue(new FileInputStream(fileConfig), DragonConfiguration.class);

        new PacketTranslatorRegistry();

        // Initiate RakNet
        sessionManager = new ProxySessionManager();

        RakNetServer.Builder<BedrockSession<UpstreamSession>> builder = RakNetServer.builder();
        builder.eventListener(new ProxyRakNetEventListener())
            .address(new InetSocketAddress(configuration.getBindAddress(), configuration.getBindPort()))
            .packet(WrappedPacket::new, 0xfe)
            .sessionManager(sessionManager)
            .sessionFactory(rakNetSession -> {
                BedrockSession<UpstreamSession> session = new BedrockSession<>(rakNetSession);
                session.setHandler(new UpstreamPacketHandler(session, this));
                return session;
            });

        raknetServer = builder.build();

        if (raknetServer.bind()) {
            logger.info("RakNet server started on {}", configuration.getBindAddress());
        } else {
            logger.error("RakNet server failed to bind to {}", configuration.getBindAddress());
        }

    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void shutdown() {
        if (!shutdownInProgress.compareAndSet(false, true)) {
            return;
        }
        logger.info("Shutting down the proxy...");

        // TODO: shutdown
        System.exit(0); // Temporary to fix hanging

        shutdown = true;
    }

    public DragonConsole getConsole() {
        return console;
    }

    public String getVersion() {
        return DragonProxy.class.getPackage().getImplementationVersion();
    }
    
    public Path getFolder() {
        return Paths.get("");
    }
}
