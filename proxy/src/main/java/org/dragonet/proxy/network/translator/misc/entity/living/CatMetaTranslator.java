package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractTameableMetaTranslator;

public class CatMetaTranslator extends AbstractTameableMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 18: // Type
                dictionary.putInt(EntityData.VARIANT, (int) metadata.getValue()); // TODO: check values?
                break;
            case 19: // Unknown
            case 20: // Unknown
                break;
            case 21: // Collar color
                if(metadata.getType() == MetadataType.BYTE) {
                    dictionary.putByte(EntityData.COLOR, (byte) (int) metadata.getValue());
                }
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
