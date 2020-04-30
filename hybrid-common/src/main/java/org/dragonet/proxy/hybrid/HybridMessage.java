package org.dragonet.proxy.hybrid;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface HybridMessage {
    ByteArrayDataOutput encode(ByteArrayDataOutput out);
    void decode(ByteArrayDataInput in);

    default void handle(AbstractHybridMessageHandler handler) {
        throw new UnsupportedOperationException();
    }

    String getId();
}
