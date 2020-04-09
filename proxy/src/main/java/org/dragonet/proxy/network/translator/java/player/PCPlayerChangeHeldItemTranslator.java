package org.dragonet.proxy.network.translator.java.player;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerFacingPacket;
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;
import org.dragonet.proxy.network.translator.PacketTranslator;
import org.dragonet.proxy.network.translator.annotations.PCPacketTranslator;

@PCPacketTranslator(packetClass = ServerPlayerChangeHeldItemPacket.class)
public class PCPlayerChangeHeldItemTranslator extends PacketTranslator<ServerPlayerChangeHeldItemPacket> {

    @Override
    public void translate(ProxySession session, ServerPlayerChangeHeldItemPacket packet) {
        CachedPlayer player = session.getCachedEntity();

        player.setSelectedHotbarSlot(packet.getSlot());

        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.setRuntimeEntityId(player.getProxyEid());
        mobEquipmentPacket.setHotbarSlot(packet.getSlot());
        mobEquipmentPacket.setItem(player.getMainHand());
        mobEquipmentPacket.setContainerId(0);
        mobEquipmentPacket.setInventorySlot(0);

        session.sendPacket(mobEquipmentPacket);
    }
}
