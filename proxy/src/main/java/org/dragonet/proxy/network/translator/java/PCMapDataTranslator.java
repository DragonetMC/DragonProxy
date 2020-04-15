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
