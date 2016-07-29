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
import org.dragonet.net.packet.minecraft.LevelEventPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.world.sound.CustomSound;

//Again, is this right? lol
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;


import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;

public class PCPlaySoundPacketTranslator implements PCPacketTranslator<ServerPlaySoundPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlaySoundPacket packet) {
        try {
            String soundName = null;

            if (BuiltinSound.class.isAssignableFrom(packet.getSound().getClass())) {
                BuiltinSound sound = (BuiltinSound) packet.getSound();
                for (Field f : BuiltinSound.class.getDeclaredFields()) {
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
                return null;
            }
            short ev = 0;
            for (Field f : LevelEventPacket.Events.class.getDeclaredFields()) {
                if (f.getType().equals(short.class) && f.getName().equalsIgnoreCase("EVENT_SOUND_" + soundName)) {
                    ev = (short) f.get(null);
                }
            }
            if (ev == 0) {
                return null;
            }
            LevelEventPacket pkSound = new LevelEventPacket();
            pkSound.eventID = (short) (LevelEventPacket.Events.EVENT_ADD_PARTICLE_MASK | ev);
            pkSound.x = (float) packet.getX();
            pkSound.y = (float) packet.getY();
            pkSound.z = (float) packet.getZ();
            return new PEPacket[]{pkSound};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
