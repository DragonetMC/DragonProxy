package org.dragonet.plugin.bukkit.compat.luckperms;

import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.MutableContextSet;
import org.bukkit.entity.Player;
import org.dragonet.plugin.bukkit.DPAddonBukkit;

public class BedrockClientContextCalculator implements ContextCalculator<Player> {
    @Override
    public MutableContextSet giveApplicableContext(Player subject, MutableContextSet accumulator) {
        accumulator.add("bedrock-client", Boolean.toString(
                DPAddonBukkit.getInstance().bedrockPlayers.contains(subject.getUniqueId())));

        return accumulator;
    }
}
