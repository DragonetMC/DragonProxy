package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.utilities.BinaryStream;

public class PCPluginMessagePacketTranslator implements IPCPacketTranslator<ServerPluginMessagePacket> {
    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPluginMessagePacket packet) {

        if(packet.getChannel().equals("DragonProxy")) {
            BinaryStream bis = new BinaryStream(packet.getData());
            String command = bis.getString();

            if(command.equals("PacketForward")) {
                boolean enabled = bis.getBoolean();
                session.getPacketProcessor().setPacketForwardMode(enabled);
            }

            return null;
        }

        return null;
    }
}
