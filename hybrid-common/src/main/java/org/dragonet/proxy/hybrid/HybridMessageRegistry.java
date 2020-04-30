package org.dragonet.proxy.hybrid;

import org.dragonet.proxy.hybrid.messages.CommandsMessage;
import org.dragonet.proxy.hybrid.messages.FormResponseMessage;
import org.dragonet.proxy.hybrid.messages.PlayerLoginMessage;
import org.dragonet.proxy.hybrid.messages.ShowFormMessage;

import java.util.HashMap;
import java.util.Map;

public class HybridMessageRegistry {
    private static Map<String, HybridMessage> clientBound = new HashMap<>();
    private static Map<String, HybridMessage> serverBound = new HashMap<>();

    static {
        serverBound.put("PlayerLogin", new PlayerLoginMessage());
        serverBound.put("FormResponse", new FormResponseMessage());

        clientBound.put("ShowForm", new ShowFormMessage());
        clientBound.put("Commands", new CommandsMessage());
    }

    public static HybridMessage getMessage(String id, boolean clientBound) {
        if(clientBound) {
            return HybridMessageRegistry.clientBound.get(id);
        }
        return serverBound.get(id);
    }
}
