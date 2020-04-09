package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

public class ZombieMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 15: // Is baby
                dictionary.getFlags().setFlag(EntityFlag.BABY, (boolean) metadata.getValue());
                break;
            case 17: // Is becoming a drowned
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
