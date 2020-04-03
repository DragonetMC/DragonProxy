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
package org.dragonet.proxy.network.translator.java.entity;

import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.nukkitx.protocol.bedrock.packet.MobArmorEquipmentPacket;
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.network.translator.types.ItemTranslator;

@Log4j2
@PCPacketTranslator(packetClass = ServerEntityEquipmentPacket.class)
public class PCEntityEquipmentTranslator extends PacketTranslator<ServerEntityEquipmentPacket> {

    @Override
    public void translate(ProxySession session, ServerEntityEquipmentPacket packet) {
        CachedEntity cachedEntity = session.getEntityCache().getByRemoteId(packet.getEntityId());
        if(cachedEntity == null) {
            return;
        }

        switch(packet.getSlot()) {
            case HELMET:
                cachedEntity.setHelmet(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                cachedEntity.sendArmor(session);
                return;
            case CHESTPLATE:
                cachedEntity.setChestplate(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                cachedEntity.sendArmor(session);
                return;
            case LEGGINGS:
                cachedEntity.setLeggings(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                cachedEntity.sendArmor(session);
                return;
            case BOOTS:
                cachedEntity.setBoots(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                cachedEntity.sendArmor(session);
                return;
            case MAIN_HAND:
                cachedEntity.setMainHand(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                break;
            case OFF_HAND:
                // TODO
                cachedEntity.setOffHand(ItemTranslator.translateSlotToBedrock(packet.getItem()));
                return;
            default:
                log.warn("Unknown slot: " + packet.getSlot().name());
                return;
        }

        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.setRuntimeEntityId(cachedEntity.getProxyEid());
        mobEquipmentPacket.setItem(cachedEntity.getMainHand());
        mobEquipmentPacket.setHotbarSlot(0); // TODO: get the slot from the cached inventory?
        mobEquipmentPacket.setInventorySlot(0);
        mobEquipmentPacket.setContainerId(0);

        session.sendPacket(mobEquipmentPacket);
    }
}
