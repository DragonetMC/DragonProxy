/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.events;

import java.lang.reflect.Method;
import java.util.Collection;
import org.pf4j.Plugin;

/**
 *
 * @author Epic
 */
public interface IEventManager {

    public abstract void registerEvents(Plugin plugin, Listener listener);

    public abstract void unregisterEvents(Listener listener);

    public abstract void unregisterEvents(Plugin plugin);

    public abstract void callEvent(Event event);

    abstract Collection<Method> getMethodsRecursively(Class<?> startClass, Class<?> exclusiveParent);
}
