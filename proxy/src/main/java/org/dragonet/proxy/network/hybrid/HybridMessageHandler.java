package org.dragonet.proxy.network.hybrid;

import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.hybrid.messages.EncryptionMessage;
import org.dragonet.proxy.network.hybrid.messages.SetEntityDataMessage;
import org.dragonet.proxy.network.hybrid.messages.ShowFormMessage;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

@Log4j2
@RequiredArgsConstructor
public class HybridMessageHandler {
    private final ProxySession session;

    // Bedrock to remote
    @Getter
    private final Int2IntMap formIdMap = new Int2IntOpenHashMap();

    // TODO: move to PlayerLoginMessage?
    public void handle(EncryptionMessage message) {
        if(message.isEncryptionEnabled() && session.getProxy().getConfiguration().getHybridConfig().isEncryption()) {
            log.warn("Hybrid encryption enabled!");
        }
    }

    public void handle(SetEntityDataMessage message) {
        CachedEntity entity = session.getEntityCache().getByRemoteId(message.getEntityId());
        if(entity == null) {
            log.warn("Received " + message.getId() + " from " + session.getUsername() + ", entity was null");
            return;
        }

        entity.getMetadata().putAll(message.getEntityDataMap());
        entity.sendMetadata(session);
    }

    public void handle(ShowFormMessage message) {
        int proxyId = session.getFormIdCounter().getAndIncrement();
        formIdMap.put(proxyId, message.getFormId());

        ModalFormRequestPacket modalFormRequestPacket = new ModalFormRequestPacket();
        modalFormRequestPacket.setFormId(proxyId);
        modalFormRequestPacket.setFormData(message.getFormData());

        session.sendPacket(modalFormRequestPacket);
    }
}
