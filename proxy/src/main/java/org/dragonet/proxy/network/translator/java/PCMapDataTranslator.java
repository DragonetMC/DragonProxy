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

import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.nukkitx.protocol.bedrock.data.MapDecoration;
import com.nukkitx.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;
import org.dragonet.proxy.util.MapDataUtils;

@Log4j2
@PCPacketTranslator(packetClass = ServerMapDataPacket.class)
public class PCMapDataTranslator extends PacketTranslator<ServerMapDataPacket> {

    @Override
    public void translate(ProxySession session, ServerMapDataPacket packet) {
        ClientboundMapItemDataPacket mapItemDataPacket = new ClientboundMapItemDataPacket();
        mapItemDataPacket.setUniqueMapId(packet.getMapId());
        mapItemDataPacket.setLocked(packet.isLocked());
        mapItemDataPacket.setScale(packet.getScale());
        mapItemDataPacket.setDimensionId(0);

        if(packet.getData() != null) {
            MapData mapData = packet.getData();

            mapItemDataPacket.setXOffset(mapData.getX());
            mapItemDataPacket.setYOffset(mapData.getY());
            mapItemDataPacket.setHeight(mapData.getRows());
            mapItemDataPacket.setWidth(mapData.getColumns());

            int[] colors = new int[mapData.getData().length];

            for (int i = 0; i < colors.length; i++) {
                colors[i] = MapDataUtils.getColor(mapData.getData()[i]).argb();
            }

            mapItemDataPacket.setColors(colors);
        }

//        for (MapIcon icon : packet.getIcons()) {
//            String displayName = icon.getDisplayName() == null ? "test" : icon.getDisplayName().getFullText();
//
//            mapItemDataPacket.getDecorations().add(new MapDecoration(1, icon.getIconRotation(), icon.getCenterX(),
//                icon.getCenterZ(), displayName, -1));
//        }

        session.sendPacket(mapItemDataPacket);
    }
}
