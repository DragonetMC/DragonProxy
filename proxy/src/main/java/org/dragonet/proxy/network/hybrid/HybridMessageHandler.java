package org.dragonet.proxy.network.hybrid;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

@Log4j2
@RequiredArgsConstructor
public class HybridMessageHandler {
    private final ProxySession session;

    public void handle(EncryptionMessage message) {
        if(message.isEncryptionEnabled() && session.getProxy().getConfiguration().getHybridConfig().isEncryption()) {
            log.warn("Hybrid encryption enabled!");
        }
    }
}
