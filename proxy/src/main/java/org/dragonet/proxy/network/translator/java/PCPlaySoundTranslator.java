/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.packet.PlaySoundPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.network.translator.misc.SoundTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
@PacketRegisterInfo(packet = ServerPlaySoundPacket.class)
public class PCPlaySoundTranslator extends PacketTranslator<ServerPlaySoundPacket> {

    @Override
    public void translate(ProxySession session, ServerPlaySoundPacket packet) {
        if(!(packet.getSound() instanceof BuiltinSound)) {
            log.info("PCPlaySoundTranslator: Custom sound received, ignoring");
            return;
        }

        BuiltinSound sound = (BuiltinSound) packet.getSound();
        String soundName = SoundTranslator.translateToBedrock(sound);

        if(soundName == null) {
            log.info(TextFormat.DARK_AQUA + "No mapping for sound: " + sound.name());
            return;
        }

        //log.info("translating sound: " + sound.name().toLowerCase() + "  ///  " + soundName);

        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        playSoundPacket.setPitch(packet.getPitch());
        playSoundPacket.setVolume(2);
        playSoundPacket.setSound(soundName);

        session.sendPacket(playSoundPacket);
    }
}

