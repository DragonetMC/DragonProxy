package org.dragonet.proxy.network.hybrid;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public void handle(HybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "Encryption";
    }
}
