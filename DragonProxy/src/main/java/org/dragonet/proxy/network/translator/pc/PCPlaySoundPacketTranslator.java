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

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.world.sound.CustomSound;
import org.spacehq.mc.protocol.data.game.world.sound.Sound;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.LevelEvent;
import sul.utils.Tuples;

public class PCPlaySoundPacketTranslator implements PCPacketTranslator<ServerPlaySoundPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerPlaySoundPacket packet) {
        try {
            String soundName = null;

            if (Sound.class.isAssignableFrom(packet.getSound().getClass())) {
                Sound sound = (Sound) packet.getSound();
                for (Field f : Sound.class.getDeclaredFields()) {
                    boolean saved = f.isAccessible();
                    f.setAccessible(true);
                    if (f.get(null).equals(sound)) {
                        soundName = f.getName();
                    }
                    f.setAccessible(saved);
                }
            } else {
                soundName = ((CustomSound) packet.getSound()).getName();
            }
            if (soundName == null) {
                return new RakNetPacket[0];
            }
            short ev = 0;
            for (Field f : LevelEvent.class.getDeclaredFields()) {
                if (f.getType().equals(short.class) && f.getName().equalsIgnoreCase(soundName)) {
                    ev = (short) f.get(null);
                }
            }
            if (ev == 0) {
                return new RakNetPacket[0];
            }
            LevelEvent pkSound = new LevelEvent();
            pkSound.eventId = (short) ev;
            pkSound.position = new Tuples.FloatXYZ((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());
            
            return fromSulPackets(pkSound);
        } catch (Exception e) {
            e.printStackTrace();
            return new RakNetPacket[0];
        }
    }

}
