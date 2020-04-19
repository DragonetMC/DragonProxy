package org.dragonet.proxy.network.hybrid;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface HybridMessage {
    ByteArrayDataOutput encode(ByteArrayDataOutput out);
    void decode(ByteArrayDataInput in);

    default void handle(HybridMessageHandler handler) { } // TODO: illegal access
    String getId();
}
