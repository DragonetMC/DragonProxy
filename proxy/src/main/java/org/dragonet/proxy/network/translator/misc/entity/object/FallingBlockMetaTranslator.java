package org.dragonet.proxy.network.translator.misc.entity.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataMap;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.BlockTranslator;
import org.dragonet.proxy.network.translator.misc.entity.IMetaTranslator;

@Log4j2
public class FallingBlockMetaTranslator implements IMetaTranslator {

    @Override
    public void translateToBedrock(ProxySession session, EntityDataMap dictionary, EntityMetadata metadata) {
        if(metadata.getId() == 7) { // Block position
            Position position = (Position) metadata.getValue();
//            dictionary.put(EntityData.VARIANT, session.getChunkCache().getBlockAt(Vector3i.from(position.getX(), position.getY(), position.getZ())));
        }
    }
}
