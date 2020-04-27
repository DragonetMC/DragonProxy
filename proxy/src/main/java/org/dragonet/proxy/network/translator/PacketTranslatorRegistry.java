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
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;
import org.dragonet.proxy.util.registry.Registry;

@Log4j2
@SuppressWarnings("unchecked")
public class PacketTranslatorRegistry<P> extends Registry {
    public static final PacketTranslatorRegistry<BedrockPacket> BEDROCK_TO_JAVA = new PacketTranslatorRegistry<>();
    public static final PacketTranslatorRegistry<Packet> JAVA_TO_BEDROCK = new PacketTranslatorRegistry<>();

    static {
        // TODO: make this shit shorter
        registerPath("org.dragonet.proxy.network.translator.java", PacketRegisterInfo.class, (info, clazz) -> {
            try {
                JAVA_TO_BEDROCK.addTranslator(((PacketRegisterInfo) info).packet(), (PacketTranslator<Packet>) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        registerPath("org.dragonet.proxy.network.translator.bedrock", PacketRegisterInfo.class, (info, clazz) -> {
            try {
                BEDROCK_TO_JAVA.addTranslator(((PacketRegisterInfo) info).packet(), (PacketTranslator<BedrockPacket>) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
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
