package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.protocol.packets.CommandRequestPacket;

public class PECommandPacketTranslator implements PEPacketTranslator<CommandRequestPacket> {
    @Override
    public Packet[] translate(UpstreamSession session, CommandRequestPacket packet) {
        ClientChatPacket chatPacket = new ClientChatPacket(packet.command);
        return new Packet[]{chatPacket};
    }
}
