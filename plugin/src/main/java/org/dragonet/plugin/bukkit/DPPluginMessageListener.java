package org.dragonet.plugin.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.dragonet.common.utilities.BinaryStream;
import org.dragonet.plugin.plugin.dpaddon.DPAddonBukkit;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.Protocol;

public class DPPluginMessageListener implements PluginMessageListener {

    private final DPAddonBukkit plugin;

    public DPPluginMessageListener(DPAddonBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if(!channel.equals("DragonProxy")) return; // not likely to happen but...
        BinaryStream bis = new BinaryStream(data);
        String command = bis.getString();
        if(command.equals("Notification")) {
            plugin.detectedBedrockPlayer(player);
        } else if (command.equals("PacketForward")) {
            processPacketForward(player, bis.getByteArray());
        }
    }

    private void processPacketForward(Player player, byte[] buffer) {
        final PEPacket packet = Protocol.decodeSingle(buffer);
        if(packet == null) return;
        BedrockPlayer bedrockPlayer = BedrockPlayer.get(player);
        if(bedrockPlayer == null) {
            player.kickPlayer("Non-bedrock player sent packet forward packets!? ");
            return;
        }
        bedrockPlayer.process(packet);
    }

}
