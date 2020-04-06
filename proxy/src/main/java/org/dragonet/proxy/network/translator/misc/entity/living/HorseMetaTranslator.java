package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractHorseMetaTranslator;

public class HorseMetaTranslator extends AbstractHorseMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 18) { // Variant
            dictionary.putInt(EntityData.VARIANT, (int) metadata.getValue());
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
