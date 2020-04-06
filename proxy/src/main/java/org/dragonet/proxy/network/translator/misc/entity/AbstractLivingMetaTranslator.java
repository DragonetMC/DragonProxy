package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

@Log4j2
public abstract class AbstractLivingMetaTranslator implements IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 7: // Hand states
                break;
            case 8: // Health
                dictionary.putFloat(EntityData.HEALTH, (float) metadata.getValue());
                break;
            case 9: // Potion effect colour
                dictionary.putInt(EntityData.POTION_COLOR, (int) metadata.getValue());
                break;
            case 10: // Is potion effect ambient
                dictionary.putByte(EntityData.POTION_AMBIENT, (boolean) metadata.getValue() ? (byte) 1 : (byte) 0);
                break;
        }
    }
}
