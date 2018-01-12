package org.dragonet.plugin.bukkit.processors;

import org.bukkit.Bukkit;
import org.dragonet.plugin.bukkit.BedrockPacketProcessor;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.plugin.bukkit.events.ModalFormResponseEvent;
import org.dragonet.protocol.packets.ModalFormResponsePacket;
import org.json.JSONArray;

public class ModalFormResponseProcessor implements BedrockPacketProcessor<ModalFormResponsePacket> {
    @Override
    public void process(BedrockPlayer bedrockPlayer, ModalFormResponsePacket packet) {
        JSONArray array = new JSONArray(packet.formData);

        ModalFormResponseEvent event = new ModalFormResponseEvent(bedrockPlayer, array);
        Bukkit.getPluginManager().callEvent(event);
    }
}
