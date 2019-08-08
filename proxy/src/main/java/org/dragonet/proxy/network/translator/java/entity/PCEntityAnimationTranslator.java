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
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.nukkitx.protocol.bedrock.packet.AnimatePacket;
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.util.TextFormat;

@Log4j2
public class PCEntityAnimationTranslator implements PacketTranslator<ServerEntityAnimationPacket> {
    public static final PCEntityAnimationTranslator INSTANCE = new PCEntityAnimationTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityAnimationPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getById(packet.getEntityId());
        if(cachedEntity == null) {
            log.info("(debug) Cached entity is null");
            return;
        }

        AnimatePacket animatePacket = new AnimatePacket();
        animatePacket.setRuntimeEntityId(packet.getEntityId());

        switch(packet.getAnimation()) {
            case SWING_ARM:
                animatePacket.setAction(AnimatePacket.Action.SWING_ARM);
                break;
            case CRITICAL_HIT:
                animatePacket.setAction(AnimatePacket.Action.CRITICAL_HIT);
                break;
            case ENCHANTMENT_CRITICAL_HIT:
                animatePacket.setAction(AnimatePacket.Action.MAGIC_CRITICAL_HIT);
                break;
            case LEAVE_BED:
                animatePacket.setAction(AnimatePacket.Action.WAKE_UP);
                break;
            default:
                log.info("(debug) Unhandled animation " + packet.getAnimation().name());
                break;
        }

        session.getBedrockSession().sendPacket(animatePacket);
    }
}
