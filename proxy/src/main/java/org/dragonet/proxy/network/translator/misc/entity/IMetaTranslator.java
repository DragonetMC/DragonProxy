package org.dragonet.proxy.network.translator.misc.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import org.dragonet.proxy.network.session.ProxySession;

public interface IMetaTranslator {

    void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata);
}
