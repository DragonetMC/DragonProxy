package org.dragonet.proxy.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.hybrid.AbstractHybridMessageHandler;
import org.dragonet.proxy.hybrid.HybridMessage;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionMessage implements HybridMessage {
    private boolean encryptionEnabled;

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        out.writeBoolean(encryptionEnabled);
        return out;
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        encryptionEnabled = in.readBoolean();
    }

    @Override
    public void handle(AbstractHybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "Encryption";
    }
}
