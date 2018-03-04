package org.dragonet.proxy.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pf4j.Plugin;

public class WrapedListener {

    private final Plugin plugin;
    private final Listener listener;
    private final Method method;
    
    public WrapedListener(Plugin plugin, Listener listener, Method method) {
        super();
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public void callEvent(Event event){
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public Plugin getPlugin() {
        return plugin;
    }

    public Listener getListener() {
        return listener;
    }

}
