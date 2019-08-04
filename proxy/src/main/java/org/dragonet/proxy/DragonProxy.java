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
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.v361.Bedrock_v361;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.dragonet.proxy.command.CommandManager;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.console.DragonConsole;
import org.dragonet.proxy.network.ProxyServerEventListener;
import org.dragonet.proxy.network.translator.types.ItemTranslator;
import org.dragonet.proxy.util.PaletteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragonProxy {
    public static final BedrockPacketCodec BEDROCK_CODEC = Bedrock_v361.V361_CODEC;
    public static final BedrockPacketCodec[] BEDROCK_SUPPORTED_CODECS = {BEDROCK_CODEC};
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
    private CommandManager commandManager;

    @Getter
    private DragonConfiguration configuration;

    @Getter
    private PaletteManager paletteManager;

    @Getter
    private PingPassthroughThread pingPassthroughThread;

    @Getter
    private ScheduledExecutorService generalThreadPool;

    @Getter
    private volatile boolean running = true;

    @Getter @Setter
    private boolean experimentalItemNBT = false;

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

    private void initialize() throws IOException {
        if(!RELEASE) {
            logger.warn("This is a development build. It may contain bugs. Do not use in production.");
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
        try {
            if(!Files.exists(Paths.get("config.yml"))) {
                Files.copy(getClass().getResourceAsStream("/config.yml"), Paths.get("config.yml"), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            logger.error("Failed to copy config file: " + ex.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        configuration = mapper.readValue(new FileInputStream("config.yml"), DragonConfiguration.class);

        generalThreadPool = Executors.newScheduledThreadPool(configuration.getThreadPoolSize());

        paletteManager = new PaletteManager();

        new ItemTranslator();

        commandManager = new CommandManager();

        pingPassthroughThread = new PingPassthroughThread(this);

        if(configuration.isPingPassthrough()) {
            generalThreadPool.scheduleAtFixedRate(pingPassthroughThread, 1, 1, TimeUnit.SECONDS);
            logger.info("Ping passthrough enabled");
        }

        BedrockServer server = new BedrockServer(new InetSocketAddress(configuration.getBindAddress(), configuration.getBindPort()));
        server.setHandler(new ProxyServerEventListener(this));

        server.bind().whenComplete((aVoid, throwable) -> {
            if (throwable == null) {
                logger.info("RakNet server started on {}", configuration.getBindAddress());
            } else {
                logger.error("RakNet server failed to bind to {}, {}", configuration.getBindAddress(), throwable.getMessage());
            }
        }).join();


        while (this.running) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void shutdown() {
        if (!shutdownInProgress.compareAndSet(false, true)) {
            return;
        }
        logger.info("Shutting down the proxy...");

        generalThreadPool.shutdown();

        this.running = false;

        synchronized (this) {
            this.notify();
        }
    }

    public String getVersion() {
        return DragonProxy.class.getPackage().getImplementationVersion();
    }
    
    public Path getFolder() {
        return Paths.get("");
    }
}
