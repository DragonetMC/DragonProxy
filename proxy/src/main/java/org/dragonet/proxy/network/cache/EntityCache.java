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
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;

public final class EntityCache {

    private final UpstreamSession upstream;
    private final CachedEntity clientEntity;
    // proxy eid -> entity
    private final Map<Long, CachedEntity> entities = Collections.synchronizedMap(new HashMap<>());
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

    public UpstreamSession getUpstream() {
        return upstream;
    }

    public Map<Long, CachedEntity> getEntities() {
        return entities;
    }

    public void reset(boolean clear) {
//        for(CachedEntity entity : entities.values())
//            entity.despawn(upstream);
        if (clear) {
            entities.clear();
            mapRemoteToClient.clear();
            mapClientToRemote.clear();
        }
        entities.put(1L, clientEntity);
        mapClientToRemote.put(clientEntity.proxyEid, clientEntity.eid);
        mapRemoteToClient.put(clientEntity.eid, clientEntity.proxyEid);
    }

    public CachedEntity getClientEntity() {
        if (!entities.containsKey(1L))
            entities.put(1L, new CachedEntity(1L, 1L, -1, EntityType.PLAYER, null, true, null));
        return entities.get(1L);
    }

    public void updateClientEntity(ServerJoinGamePacket packet) {
        CachedEntity clientEntity = entities.get(1L);
        clientEntity.eid = packet.getEntityId();
        clientEntity.dimention = packet.getDimension();
        mapClientToRemote.put(clientEntity.proxyEid, clientEntity.eid);
        mapRemoteToClient.put(clientEntity.eid, clientEntity.proxyEid);
    }

    public CachedEntity getByRemoteEID(long eid) {
        if (!mapRemoteToClient.containsKey(eid)) {
            return null;
        }
        long proxyEid = mapRemoteToClient.get(eid);
        return entities.get(proxyEid);
    }

    public CachedEntity getByLocalEID(long eid) {
        if (!mapClientToRemote.containsKey(eid)) {
            return null;
        }
        return entities.get(eid);
    }

    public CachedEntity removeByRemoteEID(long eid) {
        if (!mapRemoteToClient.containsKey(eid)) {
            return null;
        }
        long proxyEid = mapRemoteToClient.get(eid);
        CachedEntity e = entities.remove(proxyEid);
        if (e == null) {
            return null;
        }
        mapClientToRemote.remove(proxyEid);
        playerEntities.remove(e.proxyEid);
        return e;
    }

    /**
     * Cache a new entity by its spawn packet.
     *
     * @param packet
     * @return Returns null if that entity isn't supported on MCPE yet.
     */
    public CachedEntity newEntity(ServerSpawnMobPacket packet) {
        EntityType peType = EntityType.convertToPE(packet.getType());
        if (peType == null) {
            DragonProxy.getInstance().getLogger().debug("Not supported entity : " + packet.getType().name());
            return null; // Not supported
        }

        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), MagicValues.value(Integer.class, packet.getType()),
            peType, null, false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        e.pcMeta = packet.getMetadata();
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
        return e;
    }

    public CachedEntity newEntity(ServerSpawnPaintingPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.PAINTING, null, false, null);
        e.absoluteMove(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ(), 0, 0);
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
        return e;
    }

    public CachedEntity newPlayer(ServerSpawnPlayerPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.PLAYER, null, true, packet.getUUID());
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.pcMeta = packet.getMetadata();
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
        playerEntities.add(e.proxyEid);
        return e;
    }

    public CachedEntity newObject(ServerSpawnObjectPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), -1, EntityType.ITEM, packet.getType(),
            false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
        return e;
    }

    //special for Object that are not items
    public CachedEntity newEntity(ServerSpawnObjectPacket packet) {
        EntityType peType = EntityMetaTranslator.translateToPE(packet.getType());
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), MagicValues.value(Integer.class, packet.getType()),
            peType, packet.getType(), false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
        return e;
    }

    //special for exp orbs
    public CachedEntity newEntity(ServerSpawnExpOrbPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), nextClientEntityId.getAndIncrement(), 0, EntityType.EXP_ORB, ObjectType.EXP_BOTTLE, false, null);
        e.absoluteMove(packet.getX(), packet.getY(), packet.getZ(), 0, 0);
        entities.put(e.proxyEid, e);
        mapClientToRemote.put(e.proxyEid, e.eid);
        mapRemoteToClient.put(e.eid, e.proxyEid);
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
