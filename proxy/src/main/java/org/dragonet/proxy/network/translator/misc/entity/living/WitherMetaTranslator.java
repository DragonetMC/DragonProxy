package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

@Log4j2
public class WitherMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        long witherTargetId = -1;

        // Find the wither target id
        if(metadata.getId() == 15 || metadata.getId() == 16 || metadata.getId() == 17) {
            CachedEntity entity = session.getEntityCache().getByRemoteId((int) metadata.getValue());
            if(entity != null) {
                witherTargetId = entity.getProxyEid();
            }
        }

        switch(metadata.getId()) {
            case 15: // Center head target
                dictionary.putLong(EntityData.WITHER_TARGET_1, witherTargetId);
                break;
            case 16: // Left head target
                dictionary.putLong(EntityData.WITHER_TARGET_2, witherTargetId);
                break;
            case 17: // Right head target
                dictionary.putLong(EntityData.WITHER_TARGET_3, witherTargetId);
                break;
            case 18: // Invulnerable time
                dictionary.putInt(EntityData.WITHER_INVULNERABLE_TICKS, (int) metadata.getValue());
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
