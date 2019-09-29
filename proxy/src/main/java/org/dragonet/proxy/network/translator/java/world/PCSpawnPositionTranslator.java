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
package org.dragonet.proxy.network.translator.java.world;

    import com.flowpowered.math.vector.Vector3f;
    import com.flowpowered.math.vector.Vector3i;
    import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
    import com.nukkitx.protocol.bedrock.packet.SetSpawnPositionPacket;
    import lombok.AccessLevel;
    import lombok.NoArgsConstructor;
    import lombok.extern.log4j.Log4j2;
    import org.dragonet.proxy.network.session.ProxySession;
    import org.dragonet.proxy.network.translator.PacketTranslator;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PCSpawnPositionTranslator implements PacketTranslator<ServerSpawnPositionPacket> {
    public static final PCSpawnPositionTranslator INSTANCE = new PCSpawnPositionTranslator();

    @Override
    public void translate(ProxySession session, ServerSpawnPositionPacket packet) {
        SetSpawnPositionPacket spawnPositionPacket = new SetSpawnPositionPacket();
        spawnPositionPacket.setBlockPosition(new Vector3i(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ()));
        spawnPositionPacket.setSpawnForced(true);
        spawnPositionPacket.setSpawnType(SetSpawnPositionPacket.Type.PLAYER_SPAWN);

        session.getCachedEntity().setSpawnPosition(new Vector3f(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ()));

        session.getBedrockSession().sendPacket(spawnPositionPacket);
    }
}
