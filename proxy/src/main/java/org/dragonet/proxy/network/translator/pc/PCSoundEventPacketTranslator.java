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

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.LevelSoundEventPacket;
import org.dragonet.proxy.utilities.Vector3F;

public class PCSoundEventPacketTranslator implements IPCPacketTranslator<ServerPlayBuiltinSoundPacket> {

    public PCSoundEventPacketTranslator() {

    }

    public PEPacket[] translate(UpstreamSession session, ServerPlayBuiltinSoundPacket packet) {
        LevelSoundEventPacket pk = new LevelSoundEventPacket();
        pk.position = new Vector3F((float) packet.getX(), (float) packet.getY(), (float) packet.getZ());
        pk.pitch = (int) packet.getPitch();
        switch (packet.getSound()) {
            case BLOCK_GRASS_BREAK:
                pk.sound = LevelSoundEventPacket.SOUND_BREAK;
                break;
            case BLOCK_GRASS_PLACE:
                pk.sound = LevelSoundEventPacket.SOUND_PLACE;
                break;
            case BLOCK_GRASS_FALL:
                pk.sound = LevelSoundEventPacket.SOUND_FALL_SMALL;
                break;
            case BLOCK_CHEST_OPEN:
                pk.sound = LevelSoundEventPacket.SOUND_CHEST_OPEN;
                break;
            case BLOCK_CHEST_CLOSE:
                pk.sound = LevelSoundEventPacket.SOUND_CHEST_CLOSED;
                break;
            case ENTITY_ARROW_SHOOT:
                pk.sound = LevelSoundEventPacket.SOUND_BOW;
                break;
        }
        pk.isGlobal = false;
        pk.isBabyMob = false;
        pk.extraData = 0;
        pk.offset = 0;
        return new PEPacket[]{pk};
    }
}
