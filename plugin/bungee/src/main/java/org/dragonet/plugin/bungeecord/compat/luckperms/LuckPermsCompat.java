package org.dragonet.plugin.bungeecord.compat.luckperms;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import java.util.Set;
import java.util.UUID;

public class LuckPermsCompat {
    private static final LuckPermsApi api = LuckPerms.getApi();

    public static void addContextCalculator(Set<UUID> bedrockPlayers) {
        api.getContextManager()
                .registerCalculator(new BedrockClientContextCalculator(bedrockPlayers));
    }
}
