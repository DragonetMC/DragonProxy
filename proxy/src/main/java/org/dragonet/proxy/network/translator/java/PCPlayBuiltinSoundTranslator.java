/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import com.nukkitx.protocol.bedrock.packet.LevelEventGenericPacket;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import com.nukkitx.protocol.bedrock.packet.LevelSoundEventPacket;
import com.nukkitx.protocol.bedrock.packet.PlaySoundPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.SoundTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerPlayBuiltinSoundPacket.class)
public class PCPlayBuiltinSoundTranslator extends PacketTranslator<ServerPlayBuiltinSoundPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayBuiltinSoundPacket packet) {
        String soundName = SoundTranslator.translateToBedrock(packet.getSound());
        if(soundName == null) {
            //log.warn("Sound is null: " + packet.getSound().name());
            return;
        }

        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.setPosition(Vector3f.from(packet.getX(), packet.getY(), packet.getZ()));
        playSoundPacket.setPitch(packet.getPitch());
        playSoundPacket.setVolume(packet.getVolume());
        playSoundPacket.setSound(soundName);

        session.sendPacket(playSoundPacket);
    }
}

