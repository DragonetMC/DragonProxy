package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.VillagerData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;

public class ZombieVillagerMetaTranslator extends ZombieMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 18: // Is converting
                dictionary.getFlags().setFlag(EntityFlag.CONVERTING, (boolean) metadata.getValue());
                break;
            case 19: // Villager data
                VillagerData data = (VillagerData) metadata.getValue();
                // TODO
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
