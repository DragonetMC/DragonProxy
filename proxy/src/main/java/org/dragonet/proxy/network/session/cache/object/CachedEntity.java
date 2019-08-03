package org.dragonet.proxy.network.session.cache.object;

import com.flowpowered.math.vector.Vector3f;
import lombok.Getter;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CachedEntity {
    private long entityId;

    private Vector3f position;

    private Set<EntityEffectTranslator.BedrockEffect> effects = new HashSet<>();

    public CachedEntity(long entityId) {
        this.entityId = entityId;
    }
}
