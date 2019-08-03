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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.packetlib.packet.Packet;
import com.google.common.base.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.bedrock.*;
import org.dragonet.proxy.network.translator.bedrock.player.PEMovePlayerTranslator;
import org.dragonet.proxy.network.translator.java.*;
import org.dragonet.proxy.network.translator.java.entity.*;
import org.dragonet.proxy.network.translator.java.player.*;
import org.dragonet.proxy.network.translator.java.world.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class PacketTranslatorRegistry<P> {
    public static final PacketTranslatorRegistry<BedrockPacket> BEDROCK_TO_JAVA = new PacketTranslatorRegistry<>();
    public static final PacketTranslatorRegistry<Packet> JAVA_TO_BEDROCK = new PacketTranslatorRegistry<>();

    static {
        JAVA_TO_BEDROCK.addTranslator(ServerJoinGamePacket.class, PCJoinGameTranslator.INSTANCE)
            .addTranslator(ServerMultiBlockChangePacket.class, PCMultiBlockChangeTranslator.INSTANCE)
            .addTranslator(ServerDifficultyPacket.class, PCDifficultyTranslator.INSTANCE)
            .addTranslator(ServerTitlePacket.class, PCTitleTranslator.INSTANCE)
            .addTranslator(ServerEntityHeadLookPacket.class, PCEntityHeadlookPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionPacket.class, PCEntityPositionPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityPositionRotationPacket.class, PCEntityPositionRotationPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityTeleportPacket.class, PCEntityTeleportPacketTranslator.INSTANCE)
            .addTranslator(ServerEntityVelocityPacket.class, PCEntityVelocityPacketTranslator.INSTANCE)
            .addTranslator(ServerUpdateTimePacket.class, PCUpdateTimeTranslator.INSTANCE)
            .addTranslator(ServerChatPacket.class, PCChatTranslator.INSTANCE)
            .addTranslator(ServerEntityDestroyPacket.class, PCServerEntityDestroyPacketTranslator.INSTANCE)
            .addTranslator(ServerChunkDataPacket.class, PCChunkDataTranslator.INSTANCE)
            .addTranslator(ServerDisconnectPacket.class, PCDisconnectTranslator.INSTANCE)
            .addTranslator(ServerNotifyClientPacket.class, PCNotifyClientTranslator.INSTANCE)
            .addTranslator(ServerBossBarPacket.class, PCBossBarTranslator.INSTANCE)
            .addTranslator(ServerSpawnPlayerPacket.class, PCSpawnPlayerTranslator.INSTANCE)
            .addTranslator(ServerSpawnMobPacket.class, PCSpawnMobTranslator.INSTANCE)
            .addTranslator(ServerEntityEffectPacket.class, PCEntityEffectTranslator.INSTANCE)
            .addTranslator(ServerEntityRemoveEffectPacket.class, PCEntityRemoveEffectTranslator.INSTANCE)
            .addTranslator(ServerSetSlotPacket.class, PCSetSlotTranslator.INSTANCE)
            .addTranslator(ServerWindowItemsPacket.class, PCWindowItemsTranslator.INSTANCE);

        BEDROCK_TO_JAVA.addTranslator(TextPacket.class, PETextTranslator.INSTANCE)
            .addTranslator(AnimatePacket.class, PEAnimateTranslator.INSTANCE)
            .addTranslator(CommandRequestPacket.class, PECommandRequestTranslator.INSTANCE)
            .addTranslator(MovePlayerPacket.class, PEMovePlayerTranslator.INSTANCE);
    }

    private final Map<Class<?>, PacketTranslator<P>> translators = new HashMap<>();

    public void translate(ProxySession session, P packet) {
        Class<?> packetClass = packet.getClass();
        PacketTranslator<P> target = translators.get(packetClass);
        if (target == null) {
            log.info("Unhandled packet received from remote: {}", packetClass.getSimpleName());
            return;
        }
        if (session.getDownstream() == null) {
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
