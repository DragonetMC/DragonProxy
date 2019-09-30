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
package org.dragonet.proxy.network.translator.bedrock;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.nukkitx.protocol.bedrock.packet.TextPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PETextTranslator implements PacketTranslator<TextPacket> {
    public static final PETextTranslator INSTANCE = new PETextTranslator();

    @Override
    public void translate(ProxySession session, TextPacket packet) {
        if(packet.getMessage().charAt(0) == '.' && packet.getMessage().charAt(1) == '/') {
            ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage().replace("./", "/"));
            session.sendRemotePacket(chatPacket);
            return;
        }

        ClientChatPacket chatPacket = new ClientChatPacket(packet.getMessage());
        session.sendRemotePacket(chatPacket);
    }
}
