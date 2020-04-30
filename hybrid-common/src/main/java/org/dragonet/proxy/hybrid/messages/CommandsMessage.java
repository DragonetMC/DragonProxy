package org.dragonet.proxy.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import org.dragonet.proxy.hybrid.AbstractHybridMessageHandler;
import org.dragonet.proxy.hybrid.HybridMessage;

public class CommandsMessage implements HybridMessage {

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        int length = in.readInt();

        for(int i = 0; i < length; i++) {
            String name = in.readUTF();
            String description = in.readUTF();
        }
    }

    @Override
    public void handle(AbstractHybridMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String getId() {
        return "Commands";
    }
}
