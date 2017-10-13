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

import java.lang.reflect.Field;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.CustomSound;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import sul.protocol.bedrock137.play.PlaySound;
import sul.protocol.bedrock137.types.BlockPosition;
import sul.utils.Packet;

public class PCPlaySoundPacketTranslator implements PCPacketTranslator<ServerPlaySoundPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerPlaySoundPacket packet) {
        try {
            String soundName = null;

            if (BuiltinSound.class.isAssignableFrom(packet.getSound().getClass())) {
                BuiltinSound sound = (BuiltinSound) packet.getSound();
                soundName = sound.name();
            } else {
                soundName = ((CustomSound) packet.getSound()).getName();
            }
            if (soundName == null) {
                return null;
            }
            PlaySound pk = new PlaySound();
            pk.position = new BlockPosition((int) packet.getX(), (int) packet.getY(), (int) packet.getZ());
            pk.name = soundName;
            pk.volume = packet.getVolume();
            pk.pitch = packet.getPitch();
            return new Packet[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
