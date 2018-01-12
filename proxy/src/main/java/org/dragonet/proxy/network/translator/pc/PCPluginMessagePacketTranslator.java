/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.protocol.WrappedPEPacket;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.common.utilities.BinaryStream;

public class PCPluginMessagePacketTranslator implements IPCPacketTranslator<ServerPluginMessagePacket> {
    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPluginMessagePacket packet) {

        if(packet.getChannel().equals("DragonProxy")) {
            BinaryStream bis = new BinaryStream(packet.getData());
            String command = bis.getString();

            if(command.equals("PacketForward")) {
                boolean enabled = bis.getBoolean();
                session.getPacketProcessor().setPacketForwardMode(enabled);
            } else if (command.equals("SendPacket")) {
                WrappedPEPacket wrapped = new WrappedPEPacket(bis.getByteArray());
                session.sendPacket(wrapped);
            }/* else if (command.equals("PacketSubscription")) {
                TODO: packet subscription
            }*/

            return null;
        }

        return null;
    }
}
