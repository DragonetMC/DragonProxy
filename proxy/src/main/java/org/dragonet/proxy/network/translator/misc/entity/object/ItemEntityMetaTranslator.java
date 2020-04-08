package org.dragonet.proxy.network.translator.misc.entity.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import lombok.Setter;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedItemEntity;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;

public class ItemEntityMetaTranslator implements IMetaTranslator {
    @Setter
    private CachedItemEntity entity;

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 7) { // Item
            entity.setItem(ItemTranslator.translateSlotToBedrock((ItemStack) metadata.getValue()));
            entity.spawn(session);
        }
    }
}
