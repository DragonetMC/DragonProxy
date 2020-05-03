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
package org.dragonet.proxy.network.translator.java.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValueType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.BlockEntityTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerBlockValuePacket.class)
public class PCBlockValueTranslator extends PacketTranslator<ServerBlockValuePacket> {

    private static final int NOTE_BLOCK = 73;
    private static final int STICKY_PISTON = 92;
    private static final int PISTON = 99;
    private static final int MOB_SPAWNER = 143;
    private static final int CHEST = 145;
    private static final int ENDER_CHEST = 262;
    private static final int BEACON = 270;
    private static final int TRAPPED_CHEST = 321;
    private static final int END_GATEWAY = 491;
    private static final int SHULKER_BOX_LOWER = 501;
    private static final int SHULKER_BOX_HIGHER = 517;

    @Override
    public void translate(ProxySession session, ServerBlockValuePacket packet) {
        if(packet.getBlockId() == PISTON || packet.getBlockId() == STICKY_PISTON) {
            log.warn(packet.toString());
            float pushing = packet.getType() == PistonValueType.PUSHING ? 1f : 0f;
            boolean sticky = packet.getBlockId() == STICKY_PISTON;
            createPistonArm(session, packet.getPosition(), pushing, sticky);
        }
    }


    public static void createPistonArm(ProxySession session, Position position, float pushing, boolean sticky) {
        CompoundTagBuilder root = CompoundTagBuilder.builder();
        root.stringTag("id", "PistonArm")
            .floatTag("Progress", pushing)
            .byteTag("State", (byte) 1)
            .booleanTag("Sticky", sticky)
            .intTag("x", position.getX())
            .intTag("y", position.getY())
            .intTag("z", position.getZ());

        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition(Vector3i.from(position.getX(), position.getY(), position.getZ()));
        blockEntityDataPacket.setData(root.buildRootTag());

        session.sendPacket(blockEntityDataPacket);
    }
}
