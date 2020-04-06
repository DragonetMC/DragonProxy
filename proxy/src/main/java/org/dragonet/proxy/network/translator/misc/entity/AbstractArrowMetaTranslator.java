package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;

public abstract class AbstractArrowMetaTranslator implements IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 7:
                // 0x01 is critical
                // 0x02 is noclip (used by loyalty tridents when returning)
                dictionary.getFlags().setFlag(EntityFlag.CRITICAL, ((byte) metadata.getValue() & 0x01) > 0);
                break;
            case 9: // Piercing level
                break;
        }
    }
}
