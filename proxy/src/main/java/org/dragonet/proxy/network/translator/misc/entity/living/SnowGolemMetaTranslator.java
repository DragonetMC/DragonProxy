package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

public class SnowGolemMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 15) {
            // 0x10 has pumpkin hat
            // 0x00 has no pumpkin hat
            if(((byte) metadata.getValue() & 0x10) > 0) {
                dictionary.getFlags().setFlag(EntityFlag.SHEARED, false);
            } else {
                dictionary.getFlags().setFlag(EntityFlag.SHEARED, true);
            }
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
