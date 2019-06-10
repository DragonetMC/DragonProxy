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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nukkitx.protocol.bedrock.*;

import com.nukkitx.protocol.bedrock.v332.Bedrock_v332;
import com.nukkitx.protocol.bedrock.v340.Bedrock_v340;
import com.nukkitx.protocol.bedrock.v354.Bedrock_v354;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.console.DragonConsole;
import org.dragonet.proxy.network.ProxyServerEventListener;
import org.dragonet.proxy.util.PaletteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonProxy {

    public static final BedrockPacketCodec BEDROCK_CODEC = Bedrock_v354.V354_CODEC;
    public static final BedrockPacketCodec[] BEDROCK_SUPPORTED_CODECS = {Bedrock_v332.V332_CODEC, Bedrock_v340.V340_CODEC, BEDROCK_CODEC};
    public static final int[] BEDROCK_SUPPORTED_PROTOCOLS;

    static {
        BEDROCK_SUPPORTED_PROTOCOLS = new int[BEDROCK_SUPPORTED_CODECS.length];
        for (int i = 0; i < BEDROCK_SUPPORTED_CODECS.length; i++) {
            BEDROCK_SUPPORTED_PROTOCOLS[i] = BEDROCK_SUPPORTED_CODECS[i].getProtocolVersion();
        }
    }

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final boolean RELEASE = false;
    public static DragonProxy INSTANCE = null;

    private final ScheduledExecutorService timerService = Executors.unconfigurableScheduledExecutorService(
        Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("DragonProxy Ticker").setDaemon(true).build()));
    private Logger logger;
    private Injector injector;

    private AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    @Getter
    private DragonConsole console;

    @Getter
    private DragonConfiguration configuration;

    @Getter
    private PaletteManager paletteManager;

    @Getter
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
        }
    }

    private void initialize() throws Exception {
        if(!RELEASE) {
            logger.warn("This is a development build. It may contain bugs. Do not use on production.");
        }

        // Create injector, provide elements from the environment and register providers
        // TODO: Remove
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

        paletteManager = new PaletteManager();

        BedrockServer server = new BedrockServer(new InetSocketAddress(configuration.getBindAddress(), configuration.getBindPort()));
        server.setHandler(new ProxyServerEventListener(this));

        server.bind().whenComplete((aVoid, throwable) -> {
            if (throwable == null) {
                logger.info("RakNet server started on {}", configuration.getBindAddress());
            } else {
                logger.error("RakNet server failed to bind to {}, {}", configuration.getBindAddress(), throwable.getMessage());
            }
        }).join();
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

    public String getVersion() {
        return DragonProxy.class.getPackage().getImplementationVersion();
    }
    
    public Path getFolder() {
        return Paths.get("");
    }
}
