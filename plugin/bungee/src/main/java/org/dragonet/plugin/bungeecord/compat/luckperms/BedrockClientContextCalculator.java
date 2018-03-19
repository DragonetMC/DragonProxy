package org.dragonet.plugin.bungeecord.compat.luckperms;

import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.MutableContextSet;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.dragonet.plugin.bungeecord.DPAddonBungee;

public class BedrockClientContextCalculator implements ContextCalculator<ProxiedPlayer> {
    @Override
    public MutableContextSet giveApplicableContext(ProxiedPlayer subject,
            MutableContextSet accumulator) {
        accumulator.add("bedrock-client", Boolean.toString(
                DPAddonBungee.getInstance().bedrockPlayers.contains(subject.getUniqueId())));

        return accumulator;
    }
}
