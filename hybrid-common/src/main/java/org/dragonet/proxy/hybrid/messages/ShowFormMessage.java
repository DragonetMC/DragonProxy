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
public class ShowFormMessage implements HybridMessage {
    private int formId;
    private String formData;

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        out.writeInt(formId);
        out.writeUTF(formData);
        return out;
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        formId = in.readInt();
        formData = in.readUTF();
    }

    @Override
    public void handle(AbstractHybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "ShowForm";
    }
}
