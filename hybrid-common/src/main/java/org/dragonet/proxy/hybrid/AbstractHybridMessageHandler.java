package org.dragonet.proxy.hybrid;

import org.dragonet.proxy.hybrid.messages.*;

public interface AbstractHybridMessageHandler {
    default void handle(EncryptionMessage message) {}
    default void handle(PlayerLoginMessage message) {}
    default void handle(CommandsMessage message) {}
    default void handle(ShowFormMessage message) {}
    default void handle(FormResponseMessage message) {}
    default void handle(SetEntityDataMessage message) {}
}
