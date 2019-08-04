package org.dragonet.proxy.network.session.cache.object;

import com.flowpowered.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;

public class CachedPlayer extends CachedEntity {

    public CachedPlayer(long entityId) {
        super(EntityType.PLAYER, entityId);
    }

    @Override
    public void spawn(ProxySession session) {
        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUuid(session.getAuthData().getIdentity());
        addPlayerPacket.setUsername(session.getAuthData().getDisplayName());
        addPlayerPacket.setRuntimeEntityId(1);
        addPlayerPacket.setUniqueEntityId(1);
        addPlayerPacket.setPlatformChatId("");
        addPlayerPacket.setPosition(new Vector3f(0, 50, 0));
        addPlayerPacket.setMotion(Vector3f.ZERO);
        addPlayerPacket.setRotation(Vector3f.ZERO);
        addPlayerPacket.setHand(ItemData.AIR);
        addPlayerPacket.setPlayerFlags(0);
        addPlayerPacket.setCommandPermission(0);
        addPlayerPacket.setWorldFlags(0);
        addPlayerPacket.setPlayerPermission(0);
        addPlayerPacket.setCustomFlags(0);
        addPlayerPacket.setDeviceId("");

        session.getBedrockSession().sendPacket(addPlayerPacket);
        spawned = true;
    }
}
