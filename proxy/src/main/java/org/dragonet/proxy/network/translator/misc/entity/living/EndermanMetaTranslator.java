package org.dragonet.proxy.network.translator.misc.entity.living;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.network.translator.misc.ItemTranslator;
import org.dragonet.proxy.network.translator.misc.entity.AbstractInsentientMetaTranslator;

public class EndermanMetaTranslator extends AbstractInsentientMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        switch(metadata.getId()) {
            case 15: // Carried block
                if(metadata.getValue() != null) {
                    dictionary.put(EntityData.ENDERMAN_HELD_ITEM_ID, BlockTranslator.translateToBedrock((BlockState) metadata.getValue()));
                }
                break;
            case 16: // Is screaming
                dictionary.getFlags().setFlag(EntityFlag.ANGRY, (boolean) metadata.getValue()); // TODO: this is just a guess
                break;
            case 17: // Is stared at
                break;
        }
        super.translateToBedrock(session, dictionary, metadata);
    }
}
