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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.translator.java;

import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareCommandsPacket;
import com.nukkitx.protocol.bedrock.data.CommandData;
import com.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.TextFormat;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class PCDeclareCommandsTranslator implements PacketTranslator<ServerDeclareCommandsPacket> {
    public static PCDeclareCommandsTranslator INSTANCE = new PCDeclareCommandsTranslator();

    @Override
    public void translate(ProxySession session, ServerDeclareCommandsPacket packet) {
        for(CommandNode node : packet.getNodes()) {
            String parser = node.getParser() != null ? node.getParser().name() : "null";
            log.info(TextFormat.LIGHT_PURPLE + node.getName() + TextFormat.AQUA + " : " + parser);
        }
    }
}
