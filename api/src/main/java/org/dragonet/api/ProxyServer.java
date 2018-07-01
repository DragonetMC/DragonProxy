package org.dragonet.api;

import org.dragonet.api.commands.ICommandRegister;
import org.dragonet.api.configuration.IServerConfig;
import org.dragonet.api.events.IEventManager;
import org.dragonet.api.language.ILang;
import org.dragonet.api.logger.IProxyLogger;
import org.dragonet.api.sessions.IRaknetInterface;
import org.dragonet.api.sessions.ISessionRegister;
import org.dragonet.api.skins.ISkinFetcher;
import org.dragonet.api.sound.ISoundTranslator;
import org.pf4j.AbstractPluginManager;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

/**
 * The main proxy server interface.
 */
public interface ProxyServer {

    static ProxyServer getInstance() {
        return null;
    }

    IProxyLogger getLogger();

    IServerConfig getConfig();

    ILang getLang();

    ISessionRegister getSessionRegister();

    IRaknetInterface getNetwork();

    AbstractPluginManager getPluginManager();

    IEventManager getEventManager();

    ISoundTranslator getSoundTranslator();

    boolean isShuttingDown();

    ScheduledExecutorService getGeneralThreadPool();

    ICommandRegister getCommandRegister();

    String getAuthMode();

    boolean isDebug();

    Properties getProperties();

    String getVersion();

    String getMotd();

    void onTick();

    void checkArguments(String[] args);

    void shutdown();

    ISkinFetcher getSkinFetcher();
}
