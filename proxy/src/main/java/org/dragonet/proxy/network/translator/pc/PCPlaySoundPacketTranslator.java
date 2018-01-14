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

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.CustomSound;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.PlaySoundPacket;

public class PCPlaySoundPacketTranslator implements IPCPacketTranslator<ServerPlaySoundPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerPlaySoundPacket packet) {
        try {
            String soundName;

            if (BuiltinSound.class.isAssignableFrom(packet.getSound().getClass())) {
                BuiltinSound sound = (BuiltinSound) packet.getSound();
                soundName = sound.name();
            } else {
                soundName = ((CustomSound) packet.getSound()).getName();
            }
            if (soundName == null) {
                return null;
            }
            PlaySoundPacket pk = new PlaySoundPacket();
            pk.blockPosition = new BlockPosition((int) packet.getX(), (int) packet.getY(), (int) packet.getZ());
            pk.name = soundName;
            pk.volume = packet.getVolume();
            pk.pitch = packet.getPitch();
            return new PEPacket[]{pk};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
