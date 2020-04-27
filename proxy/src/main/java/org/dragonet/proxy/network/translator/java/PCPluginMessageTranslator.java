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

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.hybrid.EncryptionMessage;
import org.dragonet.proxy.network.hybrid.HybridMessage;
import org.dragonet.proxy.network.hybrid.PlayerLoginMessage;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.PacketTranslator;
import org.dragonet.proxy.util.registry.PacketRegisterInfo;

@Log4j2
@PacketRegisterInfo(packet = ServerPluginMessagePacket.class)
public class PCPluginMessageTranslator extends PacketTranslator<ServerPluginMessagePacket> {
    private static Object2ObjectMap<String, HybridMessage> hybridMessages = new Object2ObjectOpenHashMap<>();

    static {
        hybridMessages.put("PlayerLogin", new PlayerLoginMessage());
        hybridMessages.put("Encryption", new EncryptionMessage());
    }

    @Override
    public void translate(ProxySession session, ServerPluginMessagePacket packet) {
        switch(packet.getChannel()) {
            case "minecraft:brand":

                break;
            case "DragonProxy":
                ByteArrayDataInput in = ByteStreams.newDataInput(packet.getData());
                String subchannel = in.readUTF();

                HybridMessage hybridMessage = hybridMessages.get(subchannel);
                if(hybridMessage == null) {
                    log.warn("Invalid hybrid message received with name: " + subchannel);
                    return;
                }

                hybridMessage.decode(in);
                hybridMessage.handle(session.getHybridMessageHandler());
                break;
        }
    }
}
