package org.dragonet.dragonproxy.api.command;

import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CommandSource {

    void sendMessage(@NonNull Component component);
}
