package org.dragonet.proxy.network.session.cache.object;

import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;

@Getter
@Setter
public class CachedItemEntity extends CachedEntity {
    private ItemData item;

    public CachedItemEntity(long proxyEid, int remoteEid) {
        super(BedrockEntityType.ITEM, proxyEid, remoteEid);
    }

    @Override
    public void spawn(ProxySession session) {
        if(item == null) {
            return; // Wait until we receive the metadata
        }
        AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
        addItemEntityPacket.setRuntimeEntityId(proxyEid);
        addItemEntityPacket.setUniqueEntityId(proxyEid);
        addItemEntityPacket.setPosition(position);
        addItemEntityPacket.setMotion(motion);
        addItemEntityPacket.setFromFishing(false);
        addItemEntityPacket.setItemInHand(item);
        addItemEntityPacket.getMetadata().putAll(metadata);

        session.sendPacket(addItemEntityPacket);
        spawned = true;
    }
}
