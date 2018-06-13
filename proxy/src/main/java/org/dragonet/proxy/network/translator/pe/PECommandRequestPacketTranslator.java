package org.dragonet.proxy.network.translator.pe;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.translators.IPEPacketTranslator;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.CommandRequestPacket;

/**
 * Created on 2017/11/15.
 */
public class PECommandRequestPacketTranslator implements IPEPacketTranslator<CommandRequestPacket> {

    public Packet[] translate(IUpstreamSession session, CommandRequestPacket packet) {
        return new Packet[]{new ClientChatPacket(packet.command)};
    }
}
