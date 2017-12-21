package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.MobArmorEquipmentPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;

public class PCEntityEquipmentPacketTranslator implements IPCPacketTranslator<ServerEntityEquipmentPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerEntityEquipmentPacket packet) {
        CachedEntity entity = session.getEntityCache().getByRemoteEID(packet.getEntityId());
        if (entity == null) {
            if (packet.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
            return null;
        }
      
    ItemStack items = packet.getItem();
    MobArmorEquipmentPacket aeq = new MobArmorEquipmentPacket();
    switch(packet.getSlot()) {
        case HELMET:
            entity.helmet = ItemBlockTranslator.translateSlotToPE(items);
            break;
        case CHESTPLATE:
            entity.chestplate = ItemBlockTranslator.translateSlotToPE(items);
            break;
        case LEGGINGS:
            entity.leggings = ItemBlockTranslator.translateSlotToPE(items);
            break;
        case BOOTS:
            entity.boots = ItemBlockTranslator.translateSlotToPE(items);
            break;
        case MAIN_HAND:
            break;
        case OFF_HAND:
            break;
    }
    aeq.helmet = entity.helmet;
    aeq.chestplate = entity.chestplate;
    aeq.leggings = entity.leggings;
    aeq.boots = entity.boots;
    aeq.rtid = entity.proxyEid;
    return new PEPacket[]{aeq};
    }
}
