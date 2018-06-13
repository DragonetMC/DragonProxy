/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import org.dragonet.api.commands.ICommandRegister;
import org.dragonet.api.configuration.IServerConfig;
import org.dragonet.api.events.IEventManager;
import org.dragonet.api.language.ILang;
import org.dragonet.api.logger.IProxyLogger;
import org.dragonet.api.plugin.IPluginManager;
import org.dragonet.api.sessions.IRaknetInterface;
import org.dragonet.api.sessions.ISessionRegister;
import org.dragonet.api.skins.ISkinFetcher;
import org.dragonet.api.sound.ISoundTranslator;
import org.pf4j.AbstractPluginManager;

/**
 *
 * @author Epic
 */
public interface ProxyServer {

    public static ProxyServer getInstance() {
        return null;
    }

    public abstract IProxyLogger getLogger();

    public abstract IServerConfig getConfig();

    public abstract ILang getLang();

    public abstract ISessionRegister getSessionRegister();

    public abstract IRaknetInterface getNetwork();

    public abstract AbstractPluginManager getPluginManager();

    public abstract IEventManager getEventManager();

    public abstract ISoundTranslator getSoundTranslator();

    public abstract boolean isShuttingDown();

    public abstract ScheduledExecutorService getGeneralThreadPool();

    public abstract ICommandRegister getCommandRegister();

    public abstract String getAuthMode();

    public abstract boolean isDebug();

    public abstract Properties getProperties();

    public abstract String getVersion();

    public abstract String getMotd();

    public abstract void onTick();

    public abstract void checkArguments(String[] args);

    public abstract void shutdown();

    public abstract ISkinFetcher getSkinFetcher();
}
