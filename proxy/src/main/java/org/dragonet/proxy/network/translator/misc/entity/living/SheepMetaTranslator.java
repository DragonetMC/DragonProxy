package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.entity.AbstractAgeableMetaTranslator;

public class SheepMetaTranslator extends AbstractAgeableMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 16) {
            // 0x0F is colour
            // 0x10 is sheared
            dictionary.putByte(EntityData.COLOR, (byte) metadata.getValue());
            dictionary.getFlags().setFlag(EntityFlag.SHEARED, ((byte) metadata.getValue() & 0x10) > 0);
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
