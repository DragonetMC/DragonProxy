package org.dragonet.proxy.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@Log4j2
public abstract class Form {
    private String type;
    private String title;

    public CompletableFuture<JsonArray> send(ProxySession session) {
        int id = session.getFormIdCounter().incrementAndGet();

        CompletableFuture<JsonArray> future = new CompletableFuture<>();
        session.getFormCache().put(id, future);

        ModalFormRequestPacket packet = new ModalFormRequestPacket();
        packet.setFormId(id);
        packet.setFormData(serialize().toString());

        session.getBedrockSession().sendPacket(packet);
        return future;
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("title", title);
        object.addProperty("type", type);
        return object;
    }
}
