/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.api.sessions;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author Epic
 */
public interface ISessionRegister {
    
    public void onTick();

    public void newSession(IUpstreamSession session);

    public void removeSession(IUpstreamSession session);

    public IUpstreamSession getSession(String identifier);

    public Map<String, IUpstreamSession> getAll();

    public int getOnlineCount();
}
