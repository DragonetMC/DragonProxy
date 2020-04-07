package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerPluginMessagePacket.class)
public class PCPluginMessageTranslator extends PacketTranslator<ServerPluginMessagePacket> {

    @Override
    public void translate(ProxySession session, ServerPluginMessagePacket packet) {
        switch(packet.getChannel().toLowerCase()) {
            case "minecraft:brand":

                break;
            case "dragonproxy:brand":

                break;
        }
    }
}
