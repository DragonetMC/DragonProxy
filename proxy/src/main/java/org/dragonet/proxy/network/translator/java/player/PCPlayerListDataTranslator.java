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
package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.property.PropertyException;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import com.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.session.data.ClientData;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.types.MessageTranslator;
import org.dragonet.proxy.util.SkinUtils;
import org.dragonet.proxy.util.TextFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class PCPlayerListDataTranslator implements PacketTranslator<ServerPlayerListDataPacket> {
    public static final PCPlayerListDataTranslator INSTANCE = new PCPlayerListDataTranslator();

    @Override
    public void translate(ProxySession session, ServerPlayerListDataPacket packet) {
        session.getPlayerListCache().setHeader(MessageTranslator.translate(packet.getHeader()));
        session.getPlayerListCache().setFooter(MessageTranslator.translate(packet.getFooter()));
    }
}
