package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractTameableMetaTranslator;

public class WolfMetaTranslator extends AbstractTameableMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 18: // Is begging
                break;
            case 19: // Collar color
                dictionary.putByte(EntityData.COLOR, (byte) (int) metadata.getValue());
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
