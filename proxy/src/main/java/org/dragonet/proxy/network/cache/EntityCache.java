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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.MagicValues;
import lombok.Getter;
import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.network.UpstreamSession;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

public final class EntityCache {

    @Getter
    private final UpstreamSession upstream;

    @Getter
    private final Map<Integer, CachedEntity> entities = Collections.synchronizedMap(new HashMap<Integer, CachedEntity>());

    private final List<Integer> playerEntities = Collections.synchronizedList(new ArrayList<Integer>());

    public EntityCache(UpstreamSession upstream) {
        this.upstream = upstream;
        reset(false);
    }
    
    public void reset(boolean clear){
        if (clear) entities.clear();
        CachedEntity clientEntity = new CachedEntity(0, -1, null, null, true, null);
        entities.put(0, clientEntity);
    }
    
    public CachedEntity getClientEntity(){
        return entities.get(0);
    }

    public CachedEntity get(int eid) {
        return entities.get(eid);
    }

    public CachedEntity remove(int eid) {
        CachedEntity e = entities.remove(eid);
        if (e == null) {
            return null;
        }
        playerEntities.remove((Integer) eid);
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
            return null; //Not supported
        }

        CachedEntity e = new CachedEntity(packet.getEntityId(), MagicValues.value(Integer.class, packet.getType()), peType, null, false, null);
        e.x = packet.getX();
        e.y = packet.getY();
        e.z = packet.getZ();
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        e.yaw = packet.getYaw();
        e.pitch = packet.getPitch();
        e.pcMeta = packet.getMetadata();
        e.spawned = true;
        entities.put(e.eid, e);
        return e;
    }

    public CachedEntity newPlayer(ServerSpawnPlayerPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), -1, null, null, true, packet.getUUID());
        e.x = packet.getX();
        e.y = packet.getY();
        e.z = packet.getZ();
        e.yaw = packet.getYaw();
        e.pitch = packet.getPitch();
        e.pcMeta = packet.getMetadata();
        e.spawned = true;
        entities.put(e.eid, e);
        playerEntities.add(e.eid);
        return e;
    }

    public CachedEntity newObject(ServerSpawnObjectPacket packet) {
        CachedEntity e = new CachedEntity(packet.getEntityId(), -1, null, packet.getType(), false, null);
        e.x = packet.getX();
        e.y = packet.getY();
        e.z = packet.getZ();
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        e.yaw = packet.getYaw();
        e.pitch = packet.getPitch();
        e.spawned = false; //Server will update its data then we can send it. 
        entities.put(e.eid, e);
        return e;
    }

    public boolean isPlayerEntity(int eid) {
        return playerEntities.contains(eid);
    }

    public void onTick() {
        //Disabled this for now
        /*
        entities.values().stream().map((e) -> {
            e.x += e.motionX;
            e.y += e.motionY;
            e.z += e.motionZ;
            return e;
        });
        */
    }
}
