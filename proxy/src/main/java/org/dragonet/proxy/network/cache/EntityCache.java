/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.cache;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;

import org.dragonet.common.data.entity.EntityType;
import org.dragonet.proxy.network.UpstreamSession;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.packetlib.packet.Packet;
import org.dragonet.api.caches.IEntityCache;
import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.network.PEPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;

public final class EntityCache implements IEntityCache {

    private final UpstreamSession upstream;
    private final ICachedEntity clientEntity;
    // proxy eid -> entity
    private final Map<Long, ICachedEntity> entities = Collections.synchronizedMap(new HashMap<>());
    // pro
    private final Set<Long> playerEntities = Collections.synchronizedSet(new HashSet<Long>());

    // 1 is for client
    private final AtomicLong nextClientEntityId = new AtomicLong(2L);
    private final Map<Long, Long> mapRemoteToClient = Collections.synchronizedMap(new HashMap<>());
    private final Map<Long, Long> mapClientToRemote = Collections.synchronizedMap(new HashMap<>());

    public EntityCache(UpstreamSession upstream) {
        this.upstream = upstream;
        this.clientEntity = new CachedEntity(1L, 1L, -1, EntityType.PLAYER, null, true, null);
        reset(false);
    }

    @Override
    public UpstreamSession getUpstream() {
        return upstream;
    }

    @Override
    public Map<Long, ICachedEntity> getEntities() {
        return entities;
    }

    @Override
    public void reset(boolean clear) {
        for(ICachedEntity entity : entities.values())
            entity.despawn(upstream);
        if (clear) {
            entities.clear();
            mapRemoteToClient.clear();
            mapClientToRemote.clear();
        }
    }

    @Override
    public ICachedEntity getClientEntity() {
        return this.clientEntity;
    }

    @Override
    public void updateClientEntity(Packet packet) {
        if (packet instanceof ServerJoinGamePacket) {
            clientEntity.setEid(((ServerJoinGamePacket)packet).getEntityId());
            clientEntity.setDimention(((ServerJoinGamePacket)packet).getDimension());
            mapClientToRemote.put(clientEntity.getProxyEid(), clientEntity.getEid());
            mapRemoteToClient.put(clientEntity.getEid(), clientEntity.getProxyEid());
        }
    }

    @Override
    public ICachedEntity getByRemoteEID(long eid) {
        if (!mapRemoteToClient.containsKey(eid)) {
            return null;
        }
        long proxyEid = mapRemoteToClient.get(eid);
        return entities.get(proxyEid);
    }

    public ICachedEntity getByLocalEID(long eid) {
        if (!mapClientToRemote.containsKey(eid)) {
            return null;
        }
        return entities.get(eid);
    }

    public ICachedEntity removeByRemoteEID(long eid) {
        if (!mapRemoteToClient.containsKey(eid)) {
            return null;
        }
        long proxyEid = mapRemoteToClient.get(eid);
        ICachedEntity e = entities.remove(proxyEid);
        if (e == null) {
            return null;
        }
        mapClientToRemote.remove(proxyEid);
        playerEntities.remove(e.getProxyEid());
        return e;
    }

    /**
     * Cache a new entity by its spawn packet.
     *
     * @param packet
     * @return Returns null if that entity isn't supported on MCPE yet.
     */
    public ICachedEntity newEntity(ServerSpawnMobPacket packet) {
        EntityType peType = EntityType.convertToPE(packet.getType());
        if (peType == null) {
            DragonProxy.getInstance().getLogger().debug("Not supported entity : " + packet.getType().name());
            return null; // Not supported
        }

        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), MagicValues.value(Integer.class, packet.getType()),
            peType, null, false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.setMotionX(packet.getMotionX());
        e.setMotionY(packet.getMotionY());
        e.setMotionZ(packet.getMotionZ());
        e.setPcMeta(packet.getMetadata());
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        return e;
    }

    public ICachedEntity newEntity(ServerSpawnPaintingPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.PAINTING, null, false, null);
        e.absoluteMove(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ(), 0, 0);
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        return e;
    }

    public ICachedEntity newPlayer(ServerSpawnPlayerPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.PLAYER, null, true, packet.getUUID());
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.setPcMeta(packet.getMetadata());
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        playerEntities.add(e.getProxyEid());
        return e;
    }

    public ICachedEntity newObject(ServerSpawnObjectPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.ITEM, packet.getType(),
            false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.setMotionX(packet.getMotionX());
        e.setMotionY(packet.getMotionY());
        e.setMotionZ(packet.getMotionZ());
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        return e;
    }

    //special for Object that are not items
    public ICachedEntity newEntity(ServerSpawnObjectPacket packet) {
        EntityType peType = EntityMetaTranslator.translateToPE(packet.getType());
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), MagicValues.value(Integer.class, packet.getType()),
            peType, packet.getType(), false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.setMotionX(packet.getMotionX());
        e.setMotionY(packet.getMotionY());
        e.setMotionZ(packet.getMotionZ());
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        return e;
    }

    //special for exp orbs
    public ICachedEntity newEntity(ServerSpawnExpOrbPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), 0, EntityType.EXP_ORB, ObjectType.EXP_BOTTLE, false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), 0, 0);
        entities.put(e.getProxyEid(), e);
        mapClientToRemote.put(e.getProxyEid(), e.getEid());
        mapRemoteToClient.put(e.getEid(), e.getProxyEid());
        return e;
    }

    public boolean isRemoteEIDPlayerEntity(long eid) {
        long proxyEid = mapRemoteToClient.get(eid);
        return playerEntities.contains(proxyEid);
    }

    public AtomicLong getNextAtomicLong()
    {
        return nextClientEntityId;
    }

    public void onTick() {
        // Disabled this for now
        /*
         * entities.values().stream().map((e) -> { e.x += e.motionX; e.y += e.motionY;
		 * e.z += e.motionZ; return e; });
         */
    }
}
