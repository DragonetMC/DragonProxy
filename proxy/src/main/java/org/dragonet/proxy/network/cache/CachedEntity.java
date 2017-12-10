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

import org.dragonet.proxy.entity.EntityType;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;

public class CachedEntity {
	// vars
	public final long eid;
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
	public float pitch;

	public EntityMetadata[] pcMeta;
	public boolean spawned;
	public final Set<Integer> effects = Collections.synchronizedSet(new HashSet<Integer>());

	// constructor
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

	// public
	public CachedEntity relativeMove(double rx, double ry, double rz, float yaw, float pitch) {
		x += rx;
		y += ry;
		z += rz;
		this.yaw = yaw;
		this.pitch = pitch;
		return this;
	}

	public CachedEntity relativeMove(double rx, double ry, double rz) {
		x += rx;
		y += ry;
		z += rz;
		return this;
	}
	public CachedEntity relativeMove(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
		return this;
	}

	// private

}
