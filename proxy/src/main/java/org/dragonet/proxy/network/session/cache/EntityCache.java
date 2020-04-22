/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache;

import com.github.steveice10.mc.auth.data.GameProfile;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongMaps;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.cache.object.CachedEntity;
import org.dragonet.proxy.network.session.cache.object.CachedItemEntity;
import org.dragonet.proxy.network.session.cache.object.CachedPainting;
import org.dragonet.proxy.network.session.cache.object.CachedPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Getter
public class EntityCache implements Cache {
    private Long2ObjectMap<CachedEntity> entities = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
    private Object2LongMap<UUID> bossbars = new Object2LongOpenHashMap<>();

    private final AtomicLong nextClientEntityId = new AtomicLong(1L);
    private final Int2LongMap remoteToClientMap = Int2LongMaps.synchronize(new Int2LongOpenHashMap());
    private final Long2IntMap clientToRemoteMap = Long2IntMaps.synchronize(new Long2IntOpenHashMap());

    /**
     * Retrieve a cached entity from proxy entity id.
     */
    public CachedEntity getByProxyId(long entityId) {
        if(!clientToRemoteMap.containsKey(entityId)) {
            return null;
        }
        return entities.get(entityId);
    }

    /**
     * Retrieve a cached entity from a remote entity id,
     * from the remote server.
     */
    public CachedEntity getByRemoteId(int entityId) {
        if(remoteToClientMap.containsKey(entityId)) {
            return entities.get(remoteToClientMap.get(entityId));
        }
        return null;
    }

    public CachedEntity getByRemoteUUID(UUID remoteId) {
        for (CachedEntity entity : entities.values()) {
            if(entity.getJavaUuid() != null && entity.getJavaUuid().equals(remoteId)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Constructs a new cached entity.
     */
    public CachedEntity newEntity(BedrockEntityType type, int entityId) {
        CachedEntity entity = new CachedEntity(type, nextClientEntityId.getAndIncrement(), entityId);

        entities.put(entity.getProxyEid(), entity);
        clientToRemoteMap.put(entity.getProxyEid(), entity.getRemoteEid());
        remoteToClientMap.put(entity.getRemoteEid(), entity.getProxyEid());
        return entity;
    }

    /**
     * Constructs a new cached item entity.
     */
    public CachedItemEntity newItemEntity(int entityId) {
        CachedItemEntity entity = new CachedItemEntity(nextClientEntityId.getAndIncrement(), entityId);

        entities.put(entity.getProxyEid(), entity);
        clientToRemoteMap.put(entity.getProxyEid(), entity.getRemoteEid());
        remoteToClientMap.put(entity.getRemoteEid(), entity.getProxyEid());
        return entity;
    }

    /**
     * Constructs a new local entity.
     *
     * A local entity is an entity that only exists on the bedrock client,
     * and not on the remote server/
     *
     * This is used for when an entity id is expected for a certain packet on
     * bedrock but not on Java, so a fake entity must be created.
     */
    public CachedEntity newLocalEntity(BedrockEntityType type) {
        CachedEntity entity = new CachedEntity(type, nextClientEntityId.getAndIncrement());
        entities.put(entity.getProxyEid(), entity);
        return entity;
    }

    /**
     * Retrieves the next entity id from the id counter and then
     * returns it for use in the bedrock BossEventPacket.
     */
    public long newBossBar(UUID uuid) {
        long proxyEid = nextClientEntityId.getAndIncrement();
        bossbars.put(uuid, proxyEid);
        return proxyEid;
    }

    public long removeBossBar(UUID uuid) {
        long proxyEid = bossbars.get(uuid);
        bossbars.remove(uuid);
        return proxyEid;
    }

    /**
     * Constructs a new cached player.
     */
    public CachedPlayer newPlayer(int entityId, GameProfile profile) {
        return newPlayer(entityId, nextClientEntityId.getAndIncrement(), profile);
    }

    public CachedPlayer newPlayer(int entityId, long proxyId, GameProfile profile) {
        CachedPlayer entity = new CachedPlayer(proxyId, entityId, profile);

        entities.put(entity.getProxyEid(), entity);
        clientToRemoteMap.put(entity.getProxyEid(), entity.getRemoteEid());
        remoteToClientMap.put(entity.getRemoteEid(), entity.getProxyEid());
        return entity;
    }

    /**
     * Constructs a new cached painting.
     */
    public CachedPainting newPainting(int entityId, String name) {
        CachedPainting entity = new CachedPainting(nextClientEntityId.getAndIncrement(), entityId, name);

        entities.put(entity.getProxyEid(), entity);
        clientToRemoteMap.put(entity.getProxyEid(), entity.getRemoteEid());
        remoteToClientMap.put(entity.getRemoteEid(), entity.getProxyEid());
        return entity;
    }

    public CachedPlayer clonePlayer(int newEntityId, CachedPlayer player) {
        player.setRemoteEid(newEntityId);

        entities.put(player.getProxyEid(), player);
        clientToRemoteMap.put(player.getProxyEid(), player.getRemoteEid());
        remoteToClientMap.put(player.getRemoteEid(), player.getProxyEid());
        return player;
    }

    public void destroyEntity(long proxyEid) {
        entities.remove(proxyEid);
        remoteToClientMap.remove(clientToRemoteMap.get(proxyEid));
        clientToRemoteMap.remove(proxyEid);
    }

    @Override
    public void purge() {
        entities.clear();
        clientToRemoteMap.clear();
        remoteToClientMap.clear();
    }
}
