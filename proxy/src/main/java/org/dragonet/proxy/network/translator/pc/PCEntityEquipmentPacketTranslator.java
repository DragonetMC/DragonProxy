package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.api.network.PEPacket;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.protocol.packets.MobEquipmentPacket;

public class PCEntityEquipmentPacketTranslator implements IPCPacketTranslator<ServerEntityEquipmentPacket> {

    @Override
    public PEPacket[] translate(IUpstreamSession session, ServerEntityEquipmentPacket packet) {
        ICachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        ItemStack items = packet.getItem();
        boolean handModified = false;

        switch (packet.getSlot()) {
            case HELMET:
                entity.setHelmet(ItemBlockTranslator.translateSlotToPE(items));
                break;
            case CHESTPLATE:
                entity.setChestplate(ItemBlockTranslator.translateSlotToPE(items));
                break;
            case LEGGINGS:
                entity.setLeggings(ItemBlockTranslator.translateSlotToPE(items));
                break;
            case BOOTS:
                entity.setBoots(ItemBlockTranslator.translateSlotToPE(items));
                break;
            case MAIN_HAND:
                entity.setMainHand(ItemBlockTranslator.translateSlotToPE(items));
            case OFF_HAND:
                handModified = true;
                break;
        }
        entity.updateEquipment(session);

        if (handModified) {
            MobEquipmentPacket equipPacket = new MobEquipmentPacket();
            equipPacket.rtid = entity.getProxyEid();
            equipPacket.item = entity.getMainHand();
            equipPacket.inventorySlot = 0;
            equipPacket.hotbarSlot = 0;
            equipPacket.windowId = 0;
            session.sendPacket(equipPacket);
        }
        return null;

    }
}