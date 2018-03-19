package org.dragonet.plugin.bukkit.compat.luckperms;

import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.MutableContextSet;
import org.bukkit.entity.Player;
import java.util.Set;
import java.util.UUID;

public class BedrockClientContextCalculator implements ContextCalculator<Player> {
    private final Set<UUID> bedrockPlayers;

    public BedrockClientContextCalculator(Set<UUID> bedrockPlayers) {
        this.bedrockPlayers = bedrockPlayers;
    }

    @Override
    public MutableContextSet giveApplicableContext(Player subject, MutableContextSet accumulator) {
        accumulator.add("bedrock-client",
                Boolean.toString(bedrockPlayers.contains(subject.getUniqueId())));

        return accumulator;
    }
}
