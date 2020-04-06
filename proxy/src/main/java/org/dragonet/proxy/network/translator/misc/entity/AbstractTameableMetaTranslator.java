package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;

import java.util.UUID;

@Log4j2
public abstract class AbstractTameableMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 16:
                // 0x01 is sitting
                // 0x02 is angry (only used with wolves)
                // 0x04 is tamed
                dictionary.getFlags().setFlag(EntityFlag.SITTING, ((byte) metadata.getValue() & 0x01) > 0);
                dictionary.getFlags().setFlag(EntityFlag.ANGRY, ((byte) metadata.getValue() & 0x02) > 0);
                dictionary.getFlags().setFlag(EntityFlag.TAMED, ((byte) metadata.getValue() & 0x04) > 0);
                break;
            case 17: // Owner
                if(metadata.getValue() != null) {
                    CachedEntity owner = session.getEntityCache().getByRemoteUUID((UUID) metadata.getValue());
                    long ownerId = owner != null ? owner.getProxyEid() : session.getCachedEntity().getProxyEid();

                    dictionary.putLong(EntityData.OWNER_EID, ownerId);
                }
                break;
        }
    }
}
