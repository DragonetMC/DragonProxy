package org.dragonet.proxy.network.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import org.dragonet.proxy.network.hybrid.HybridMessage;
import org.dragonet.proxy.network.hybrid.HybridMessageHandler;

@Getter
public class ShowFormMessage implements HybridMessage {
    private int formId;
    private String formData;

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        formId = in.readInt();
        formData = in.readUTF();
    }

    @Override
    public void handle(HybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "ShowForm";
    }
}
