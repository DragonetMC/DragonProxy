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

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.proxy.network.session.UpstreamSession;
import org.dragonet.proxy.network.translator.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PacketTranslatorRegistry {

    private static Logger logger;

    private static final Map<Class<? extends BedrockPacket>, IPacketTranslator<? extends BedrockPacket>> bedrockToJava = new HashMap<>();
    private static final Map<Class<? extends Packet>, IPacketTranslator<? extends Packet>> javaToBedrock = new HashMap<>();

    public PacketTranslatorRegistry() {
        logger = LoggerFactory.getLogger(PacketTranslatorRegistry.class);

        // Java to Bedrock
        //javaToBedrock.put(ServerDifficultyPacket.class, new PCServerDifficultyTranslator());
        //javaToBedrock.put(ServerPlayerAbilitiesPacket.class, new PCServerPlayerAbilitiesTranslator());
        //javaToBedrock.put(ServerTitlePacket.class, new PCServerTitleTranslator());
    }

    public static void translateToBedrock(BedrockSession<UpstreamSession> session, Packet packet) {
        IPacketTranslator<Packet> target = (IPacketTranslator<Packet>) javaToBedrock.get(packet.getClass());
        if(target == null) {
            logger.warn("Packet not translated " + packet.getClass().getSimpleName());
            return;
        }

        logger.info("Translating packet " + packet.getClass().getSimpleName());
        target.translate(session, packet);
    }

    public static void translateToJava(BedrockSession<UpstreamSession> session, BedrockPacket packet) {
        IPacketTranslator<BedrockPacket> target = (IPacketTranslator<BedrockPacket>) bedrockToJava.get(packet.getClass());
        if(target == null) {
            logger.warn("Packet not translated " + packet.getClass().getSimpleName());
            return;
        }
        logger.info("Translating packet " + packet.getClass().getSimpleName());
        target.translate(session, packet);
    }
}
