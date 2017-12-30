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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.protocol.type.Slot;

import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import java.util.HashMap;
import java.util.Map;
import org.dragonet.proxy.data.entity.PEEntityAttribute;
import org.dragonet.proxy.utilities.BlockPosition;

public class CachedEntity {

    public long eid;
    public final long proxyEid;
    public final int pcType;
    public final EntityType peType;
    public final ObjectType objType;

    public final boolean player;
    public final UUID playerUniqueId;

    public double x;
    public double y;
    public double z;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float yaw;
    public float headYaw;
    public float pitch;

    public boolean shouldMove = false;
    public BlockPosition spawnPosition;

    public Slot helmet;
    public Slot chestplate;
    public Slot leggings;
    public Slot boots;
    public Slot mainHand;

    // cache riding datas for dismount
    public long riding = 0;
    public Set<Long> passengers = new HashSet();

    public EntityMetadata[] pcMeta;
    public boolean spawned;
    public final Set<Integer> effects = Collections.synchronizedSet(new HashSet<Integer>());
    public Map<Integer, PEEntityAttribute> attributes = Collections.synchronizedMap(new HashMap());

    public CachedEntity(long eid, long proxyEid, int pcType, EntityType peType, ObjectType objType, boolean player,
            UUID playerUniqueId) {
        super();

        this.eid = eid;
        this.proxyEid = proxyEid;
        this.pcType = pcType;
        this.peType = peType;
        this.objType = objType;
        this.player = player;
        this.playerUniqueId = playerUniqueId;
    }

    public CachedEntity relativeMove(double rx, double ry, double rz, float yaw, float pitch) {
        if (rx != 0 || ry != 0 || rz != 0 || yaw != 0 || pitch != 0)
        {
            this.x += rx;
            this.y += ry;
            this.z += rz;
            this.yaw = yaw;
            this.pitch = pitch;
            this.shouldMove = true;
        }
        return this;
    }

    public CachedEntity absoluteMove(double x, double y, double z, float yaw, float pitch) {
        if (x != 0 || y != 0 || z != 0 || yaw != 0 || pitch != 0)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.shouldMove = true;
        }
        return this;
    }

    public CachedEntity relativeMove(double rx, double ry, double rz) {
        this.relativeMove(rx, ry, rz, 0, 0);
        return this;
    }

    public CachedEntity relativeMove(float yaw, float pitch) {
        this.relativeMove(yaw, pitch);
        return this;
    }
}
