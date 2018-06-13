package org.dragonet.proxy.events;

import org.dragonet.api.events.WrapedListener;
import org.dragonet.api.events.HandlerList;
import org.dragonet.api.events.EventHandler;
import org.dragonet.api.events.Listener;
import org.dragonet.api.events.Event;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

import org.dragonet.proxy.DragonProxy;
import org.pf4j.Plugin;
import org.pf4j.PluginState;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginStateListener;

import com.google.common.collect.Lists;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.events.IEventManager;

public class EventManager implements IEventManager {

    private ProxyServer proxy;
    private HashMap<Class<? extends Event>, HandlerList> usedHandlers = new HashMap<>();
    
    public EventManager(ProxyServer proxy){
        this.proxy = proxy;
        proxy.getPluginManager().addPluginStateListener(new PluginStateListener() {
            
            @Override
            public void pluginStateChanged(PluginStateEvent event) {
                if(event.getPluginState() == PluginState.DISABLED || event.getPluginState() == PluginState.STOPPED){
                    unregisterEvents(event.getPlugin().getPlugin());
                }
            }
        });
    }
    
    @Override
    public void registerEvents(Plugin plugin, Listener listener){
        Collection<Method> methods = getMethodsRecursively(listener.getClass(), Event.class);
        for(Method method : methods){
            EventHandler handler = method.getAnnotation(EventHandler.class);
            if(handler != null){
                if(method.getParameters().length != 1 || (method.getParameters()[0].getType()).isAssignableFrom(Event.class)){
                    proxy.getLogger().warning("The EventHandler method '" + method.getName() + "' must have exactly 1 parameter extending Event!");
                    continue;
                }
                try {
                    Field handlerField = null;
                    try{
                        handlerField = method.getParameters()[0].getType().getDeclaredField("handlerList");
                        handlerField.setAccessible(true);
                    }catch(Exception ex){
                        proxy.getLogger().warning("The Event " + method.getParameters()[0].getType().getName() + " is missing this line: 'private static final HandlerList handlerList = new HandlerList();'");
                        continue;
                    }
                    HandlerList eventHandler = (HandlerList) handlerField.get(null);
                    method.setAccessible(true);
                    eventHandler.registerListener(new WrapedListener(plugin, listener, method));
                    usedHandlers.put((Class<? extends Event>) method.getParameters()[0].getType(), eventHandler);
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void unregisterEvents(Listener listener){
        for(HandlerList handler : usedHandlers.values()){
            handler.unregisterListener(listener);
        }
    }
      
    @Override
    public void unregisterEvents(Plugin plugin){
        for(HandlerList handler : usedHandlers.values()){
            handler.unregisterListener(plugin);
        }
    }
    
    @Override
    public void callEvent(Event event){
        HandlerList handler = usedHandlers.get(event.getClass());
        if(handler != null){
            for(WrapedListener wl : handler.getHandlerList()){
                try{
                    wl.callEvent(event);
                }catch(Exception ex){
                    proxy.getLogger().warning("Error when passing event " + event.getClass().getName() + " to " + wl.getListener().getClass().getName() + " by " + wl.getPlugin().getWrapper().getPluginId());
                }
            }
        }
    }
    
    @Override
    public Collection<Method> getMethodsRecursively(Class<?> startClass, Class<?> exclusiveParent) {
        Collection<Method> methods = Lists.newArrayList(startClass.getDeclaredMethods());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            methods.addAll(getMethodsRecursively(parentClass, exclusiveParent));
        }

        return methods;
    }

    
}
