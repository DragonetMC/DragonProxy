package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;

public abstract class AbstractInsentientMetaTranslator extends AbstractLivingMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 16) {
            // 0x01 is no ai
            // 0x02 is left handed
            //dictionary.getFlags().setFlag(EntityFlag.NO_AI, ((byte) metadata.getValue() & 0x01) > 0);
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
