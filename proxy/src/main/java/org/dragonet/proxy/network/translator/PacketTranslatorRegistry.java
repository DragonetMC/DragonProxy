/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator;

import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.packetlib.packet.Packet;
import com.google.common.base.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.bedrock.*;
import org.dragonet.proxy.network.translator.java.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class PacketTranslatorRegistry<P> {
    public static final PacketTranslatorRegistry<BedrockPacket> BEDROCK_TO_JAVA = new PacketTranslatorRegistry<>();
    public static final PacketTranslatorRegistry<Packet> JAVA_TO_BEDROCK = new PacketTranslatorRegistry<>();

    static {
        JAVA_TO_BEDROCK.addTranslator(ServerJoinGamePacket.class, PCJoinGamePacketTranslator.INSTANCE)
            .addTranslator(ServerMultiBlockChangePacket.class, PCMultiBlockChangePacketTranslator.INSTANCE)
            .addTranslator(ServerDifficultyPacket.class, PCDifficultyPacketTranslator.INSTANCE)
            .addTranslator(ServerTitlePacket.class, PCTitlePacketTranslator.INSTANCE)
            .addTranslator(ServerEntityHeadLookPacket.class, PCEntityHeadlookPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionPacket.class, PCEntityPositionPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionRotationPacket.class, PCEntityPositionRotationPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityTeleportPacket.class, PCEntityTeleportPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityVelocityPacket.class, PCEntityVelocityPacketTranslator.INSTANCE)
            .addTranslator(ServerUpdateTimePacket.class, PCUpdateTimePacketTranslator.INSTANCE)
            .addTranslator(ServerChatPacket.class, PCServerChatPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityDestroyPacket.class, PCServerEntityDestroyPacketTranslator.INSTANCE)
            .addTranslator(ServerChunkDataPacket.class, PCChunkDataPacketTranslator.INSTANCE)
            .addTranslator(ServerDisconnectPacket.class, PCDisconnectPacketTranslator.INSTANCE);

        BEDROCK_TO_JAVA.addTranslator(TextPacket.class, PETextPacketTranslator.INSTANCE);
    }

    private final Map<Class<?>, PacketTranslator<P>> translators = new HashMap<>();

    public void translate(ProxySession session, P packet) {
        Class<?> packetClass = packet.getClass();
        PacketTranslator<P> target = translators.get(packetClass);
        if (target == null) {
            //log.warn("Could not translate packet {}", packetClass.getSimpleName());
            return;
        }
        target.translate(session, packet);
    }

    @SuppressWarnings("unchecked")
    private <T extends P> PacketTranslatorRegistry<P> addTranslator(Class<T> clazz, PacketTranslator<T> packetTranslator) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(packetTranslator, "packetTranslator");
        translators.put(clazz, (PacketTranslator<P>) packetTranslator);
        return this;
    }
}
