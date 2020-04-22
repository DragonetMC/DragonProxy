package org.dragonet.proxy.network.session.cache.object;

import com.nukkitx.nbt.CompoundTagBuilder;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.network.session.ProxySession;

@Log4j2
@Getter
@Setter
public class CachedItemFrame extends CachedEntity {
    private ItemData item;

    public CachedItemFrame(long proxyEid, int remoteEid) {
        super(null, proxyEid, remoteEid);
    }

    @Override
    public void spawn(ProxySession session) {
        if(item == null) {
            return;
        }
        session.getChunkCache().sendFakeBlock(session, 2603, position.toInt());

        CompoundTagBuilder root = CompoundTagBuilder.builder();
        root.stringTag("id", "ItemFrame");
        root.floatTag("ItemDropChance", 1f);
        root.floatTag("ItemRotation", 0f);
        root.tag(item.getTag());
        root.intTag("x", position.getFloorX());
        root.intTag("y", position.getFloorY());
        root.intTag("z", position.getFloorZ());

        log.warn(root);

        BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();
        blockEntityDataPacket.setBlockPosition(position.toInt());
        blockEntityDataPacket.setData(root.buildRootTag());

        session.sendPacket(blockEntityDataPacket);
        spawned = true;
    }
}
