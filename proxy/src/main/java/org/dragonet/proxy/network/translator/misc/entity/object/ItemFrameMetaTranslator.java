package org.dragonet.proxy.network.translator.misc.entity.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedItemFrame;
import org.dragonet.proxy.network.translator.ItemTranslatorRegistry;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;

public class ItemFrameMetaTranslator extends IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 7: // Item
                CachedItemFrame itemFrame = (CachedItemFrame) this.entity;
                itemFrame.setItem(ItemTranslatorRegistry.translateSlotToBedrock((ItemStack) metadata.getValue()));
                itemFrame.spawn(session);
                break;
            case 8: // Rotation

                break;
        }
    }
}
