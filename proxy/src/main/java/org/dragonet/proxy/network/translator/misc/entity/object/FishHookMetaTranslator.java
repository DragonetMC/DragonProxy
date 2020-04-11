package org.dragonet.proxy.network.translator.misc.entity.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;

@Log4j2
public class FishHookMetaTranslator implements IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 7) { // Hooked entity id
            // TODO: assign this to the actual shooter if possible
            dictionary.putLong(EntityData.OWNER_EID, session.getCachedEntity().getProxyEid());
        }
    }
}
