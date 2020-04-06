package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

import java.util.UUID;

public abstract class AbstractHorseMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 16:
                // 0x02 is tame
                // 0x04 is saddled
                // 0x08 is has bred
                // 0x10 is eating
                // 0x40 is mouth open
                dictionary.getFlags().setFlag(EntityFlag.TAMED, ((byte) metadata.getValue() & 0x02) > 0);
                dictionary.getFlags().setFlag(EntityFlag.SADDLED, ((byte) metadata.getValue() & 0x04) > 0);
                dictionary.getFlags().setFlag(EntityFlag.EATING, ((byte) metadata.getValue() & 0x10) > 0);
                break;
            case 17: // Owner
                if(metadata.getValue() != null) {
                    CachedEntity owner = session.getEntityCache().getByRemoteUUID((UUID) metadata.getValue());
                    long ownerId = owner != null ? owner.getProxyEid() : session.getCachedEntity().getProxyEid();

                    dictionary.putLong(EntityData.OWNER_EID, ownerId);
                }
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
