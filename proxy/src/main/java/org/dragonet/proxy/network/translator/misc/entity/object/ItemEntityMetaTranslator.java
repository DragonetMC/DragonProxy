package org.dragonet.proxy.network.translator.misc.entity.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;

public class ItemEntityMetaTranslator implements IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 7) { // Item

        }
    }
}
