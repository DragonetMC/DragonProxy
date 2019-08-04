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
package org.dragonet.proxy;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import lombok.Getter;

public class PingPassthroughThread implements Runnable {

    private DragonProxy proxy;

    public PingPassthroughThread(DragonProxy proxy) {
        this.proxy = proxy;
    }

    @Getter
    private ServerStatusInfo info;

    private Client client;

    @Override
    public void run() {
        try {
            this.client = new Client(proxy.getConfiguration().getRemoteAddress(), proxy.getConfiguration().getRemotePort(), new MinecraftProtocol(SubProtocol.STATUS), new TcpSessionFactory());
            this.client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
                this.info = info;
                this.client.getSession().disconnect(null);
            });

            client.getSession().connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
