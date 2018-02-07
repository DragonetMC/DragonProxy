package org.dragonet.plugin.bukkit.processors;

import com.google.gson.JsonArray;
import org.bukkit.Bukkit;
import org.dragonet.common.utilities.JsonUtil;
import org.dragonet.plugin.bukkit.BedrockPacketProcessor;
import org.dragonet.plugin.bukkit.BedrockPlayer;
import org.dragonet.plugin.bukkit.events.ModalFormResponseEvent;
import org.dragonet.protocol.packets.ModalFormResponsePacket;

public class ModalFormResponseProcessor implements BedrockPacketProcessor<ModalFormResponsePacket> {
    @Override
    public void process(BedrockPlayer bedrockPlayer, ModalFormResponsePacket packet) {
        JsonArray array = JsonUtil.parseArray(packet.formData);

        ModalFormResponseEvent event = new ModalFormResponseEvent(bedrockPlayer, array);
        Bukkit.getPluginManager().callEvent(event);
    }
}
