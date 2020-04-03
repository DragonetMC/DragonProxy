/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockServer;
import com.nukkitx.protocol.bedrock.v389.Bedrock_v389;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.command.CommandManager;
import org.dragonet.proxy.configuration.DragonConfiguration;
import org.dragonet.proxy.configuration.lang.MinecraftLanguage;
import org.dragonet.proxy.console.DragonConsole;
import org.dragonet.proxy.metrics.MetricsManager;
import org.dragonet.proxy.network.ProxyServerEventListener;
import org.dragonet.proxy.network.translator.PacketTranslatorRegistry;
import org.dragonet.proxy.network.translator.types.BlockTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;
import org.dragonet.proxy.util.PaletteManager;
import org.dragonet.proxy.util.SkinUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class DragonProxy {
    public static final BedrockPacketCodec BEDROCK_CODEC = Bedrock_v389.V389_CODEC;
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

    private long startTime;
    private int bindPort;

    /**
     * Constructs a new instance of the DragonProxy class.
     * This is the main class for the proxy (although the command line option parsing is in the `bootstrap` module)
     *
     * @param bedrockPort a custom port provided from a command line option
     *                    to override the bind port in the config
     */
    public DragonProxy(int bedrockPort) {
        INSTANCE = this;

        startTime = System.currentTimeMillis();
        bindPort = bedrockPort;

        log.info("Welcome to DragonProxy version " + getVersion());

        // Initialize services
        try {
            initialize();
        } catch (Throwable th) {
            log.error("A fatal error occurred while initializing the proxy!", th);
            System.exit(1);
        }
    }

    private void initialize() throws IOException {
        if(!RELEASE) {
            log.warn("This is a development build. It may contain bugs. Do not use in production.");
        }

        // Load configuration
        try {
            if(!Files.exists(Paths.get("config.yml"))) {
                Files.copy(getClass().getResourceAsStream("/config.yml"), Paths.get("config.yml"), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            log.error("Failed to copy config file: " + ex.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        configuration = mapper.readValue(new FileInputStream("config.yml"), DragonConfiguration.class);;

        generalThreadPool = Executors.newScheduledThreadPool(configuration.getThreadPoolSize());

        paletteManager = new PaletteManager();

        new PacketTranslatorRegistry();
        new ItemTranslator();
        new BlockTranslator();
        new SkinUtils();
        new MinecraftLanguage();

        // Initialize metrics
        new MetricsManager(this);

        commandManager = new CommandManager();

        pingPassthroughThread = new PingPassthroughThread(this);

        if(configuration.isPingPassthrough()) {
            generalThreadPool.scheduleAtFixedRate(pingPassthroughThread, 1, 10, TimeUnit.SECONDS);
            log.info("Ping passthrough enabled");
        }

        BedrockServer server = new BedrockServer(new InetSocketAddress(configuration.getBindAddress(), configuration.getBindPort()));
        server.setHandler(new ProxyServerEventListener(this));

        server.bind().whenComplete((aVoid, throwable) -> {
            if (throwable == null) {
                log.info("RakNet server started on {}", configuration.getBindAddress());
            } else {
                log.error("RakNet server failed to bind to {}, {}", configuration.getBindAddress(), throwable.getMessage());
            }
        }).join();

        double bootTime = (System.currentTimeMillis() - startTime) / 1000d;
        log.info("Done ({}s)!", new DecimalFormat("#.##").format(bootTime));

        console = new DragonConsole(this);
        console.start();

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
        log.info("Shutting down the proxy...");

        generalThreadPool.shutdown();

        this.running = false;

        System.exit(0); // Fix hanging

        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Returns the version of DragonProxy.
     */
    public String getVersion() {
        String version = DragonProxy.class.getPackage().getImplementationVersion();
        return version == null ? "DragonProxy (unpackaged)" : version;
    }
}
