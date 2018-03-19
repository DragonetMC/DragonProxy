package org.dragonet.plugin.bungeecord.compat.luckperms;

import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.MutableContextSet;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.Set;
import java.util.UUID;

public class BedrockClientContextCalculator implements ContextCalculator<ProxiedPlayer> {
    private final Set<UUID> bedrockPlayers;

    public BedrockClientContextCalculator(Set<UUID> bedrockPlayers) {
        this.bedrockPlayers = bedrockPlayers;
    }

    @Override
    public MutableContextSet giveApplicableContext(ProxiedPlayer subject,
            MutableContextSet accumulator) {
        accumulator.add("bedrock-client",
                Boolean.toString(bedrockPlayers.contains(subject.getUniqueId())));

        return accumulator;
    }
}
