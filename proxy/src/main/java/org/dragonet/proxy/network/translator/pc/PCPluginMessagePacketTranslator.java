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

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.protocol.WrappedPEPacket;
import org.dragonet.protocol.packets.StopSoundPacket;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.common.utilities.BinaryStream;

public class PCPluginMessagePacketTranslator implements IPCPacketTranslator<ServerPluginMessagePacket> {

    public static final String CHANNEL_DRAGONPROXY = "DragonProxy";
    public static final String CHANNEL_MC_BOPEN = "MC|BOpen";
    public static final String CHANNEL_MC_BRAND = "MC|Brand";
    public static final String CHANNEL_MC_DEBUG_PATH = "MC|DebugPath";
    public static final String CHANNEL_MC_DEBUG_NEIGHBORS_UPDATE = "MC|DebugNeighborsUpdate";
    public static final String CHANNEL_MC_STOP_SOUND = "MC|StopSound";
    public static final String CHANNEL_MC_TR_LIST = "MC|TrList";

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerPluginMessagePacket packet) {
        String channel = packet.getChannel();
        BinaryStream bis = new BinaryStream(packet.getData());

        if (channel.equals(CHANNEL_DRAGONPROXY)) {
            String command = bis.getString();

            if (command.equals("PacketForward")) {
                boolean enabled = bis.getBoolean();
                session.getPacketProcessor().setPacketForwardMode(enabled);
            } else if (command.equals("SendPacket")) {
                WrappedPEPacket wrapped = new WrappedPEPacket(bis.getByteArray());
                session.sendPacket(wrapped);
            } else if (command.equals("PacketSubscription")) {
                // TODO: packet subscription
            }
        } else if (channel.equals(CHANNEL_MC_BOPEN)) {
            // TODO
        } else if (channel.equals(CHANNEL_MC_BRAND)) {
            // TODO
        } else if (channel.equals(CHANNEL_MC_DEBUG_PATH)) {
            // TODO
        } else if (channel.equals(CHANNEL_MC_DEBUG_NEIGHBORS_UPDATE)) {
            // TODO
        } else if (channel.equals(CHANNEL_MC_STOP_SOUND)) {
            String category = bis.getString();
            String s = bis.getString();
            try {
                BuiltinSound sound = MagicValues.key(BuiltinSound.class, s);
                if (DragonProxy.getInstance().getSoundTranslator().isTranslatable(sound)
                        && !DragonProxy.getInstance().getSoundTranslator().isIgnored(sound)) {
                    StopSoundPacket ssp = new StopSoundPacket();
                    ssp.name = DragonProxy.getInstance().getSoundTranslator().translate(sound);
                    return new PEPacket[]{ssp};
                }
            } catch (IllegalArgumentException e) {

            }
        } else if (channel.equals(CHANNEL_MC_TR_LIST)) {
            // TODO
        }

        return null;
    }
}
