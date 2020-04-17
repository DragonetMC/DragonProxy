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
package org.dragonet.proxy.network.translator;

import com.github.steveice10.packetlib.packet.Packet;
import com.google.common.base.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PEPacketTranslator;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@SuppressWarnings("unchecked")
public class PacketTranslatorRegistry<P> {
    public static final PacketTranslatorRegistry<BedrockPacket> BEDROCK_TO_JAVA = new PacketTranslatorRegistry<>();
    public static final PacketTranslatorRegistry<Packet> JAVA_TO_BEDROCK = new PacketTranslatorRegistry<>();

    static {
        // Load Java translators
        for(Class clazz : new Reflections("org.dragonet.proxy.network.translator.java")
            .getTypesAnnotatedWith(PCPacketTranslator.class)) {

            PCPacketTranslator annotation = ((PCPacketTranslator) clazz.getAnnotation(PCPacketTranslator.class));

            try {
                JAVA_TO_BEDROCK.addTranslator(annotation.packetClass(), (PacketTranslator<Packet>) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Load Bedrock translators
        for(Class clazz : new Reflections("org.dragonet.proxy.network.translator.bedrock")
            .getTypesAnnotatedWith(PEPacketTranslator.class)) {

            PEPacketTranslator annotation = ((PEPacketTranslator) clazz.getAnnotation(PEPacketTranslator.class));

            try {
                BEDROCK_TO_JAVA.addTranslator(annotation.packetClass(), (PacketTranslator<BedrockPacket>) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private final Object2ObjectMap<Class<?>, PacketTranslator<P>> translators = new Object2ObjectOpenHashMap<>();

    public void translate(ProxySession session, P packet) {
        Class<?> packetClass = packet.getClass();
        PacketTranslator<P> target = translators.get(packetClass);
        if (target == null) {
            //log.info("Unhandled packet received from remote: {}", packetClass.getSimpleName());
            return;
        }
        if (session.getDownstream() == null || session.getBedrockSession().isClosed()) {
            return;
        }
        //log.trace("Translating packet: " + packetClass.getSimpleName());
        target.translate(session, packet);
    }

    @SuppressWarnings("unchecked")
    private PacketTranslatorRegistry<P> addTranslator(Class<?> clazz, PacketTranslator<P> packetTranslator) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(packetTranslator, "packetTranslator");
        translators.put(clazz, packetTranslator);
        return this;
    }
}
