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

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.dragonet.common.data.entity.EntityType;
import org.dragonet.common.maths.AxisAlignedBB;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.common.data.inventory.Slot;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;

import java.util.HashMap;
import java.util.Map;

import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.data.entity.meta.type.ByteArrayMeta;
import org.dragonet.common.data.entity.meta.type.SlotMeta;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.protocol.packets.*;
import org.dragonet.common.maths.BlockPosition;

public class CachedEntity {

    public long eid;
    public final long proxyEid;
    public final int pcType;
    public final EntityType peType;
    public final ObjectType objType;

    public final boolean player;
    public final UUID playerUniqueId;

    public int dimention;
    public double x;
    public double y;
    public double z;
    public int scale = 1;
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

    public int foodPacketCount = 0;
    public long lastFoodPacketTime;

    public boolean isOnStairs = false;
    public AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    // cache riding datas for dismount
    public long riding = 0;
    public Set<Long> passengers = new HashSet();

    public EntityMetadata[] pcMeta;
    public boolean spawned = false;
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
        if (rx != 0 || ry != 0 || rz != 0 || yaw != 0 || pitch != 0) {
            this.x += rx;
            this.y += ry;
            this.z += rz;
            if (yaw != 0)
                this.yaw = yaw;
            if (pitch != 0)
                this.pitch = pitch;
            this.shouldMove = true;
        }
        return this;
    }

    public CachedEntity absoluteMove(double x, double y, double z, float yaw, float pitch) {
        double radius = this.getWidth() / 2d;
        if (x != 0 || y != 0 || z != 0 || yaw != 0 || pitch != 0) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.shouldMove = true;
            this.boundingBox.setBounds(x - radius, y, z - radius, x + radius, y + (this.getHeight() * this.scale), z
                    + radius);
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

    public float getHeight() {
        return 1.8f;
    }

    public float getWidth() {
        return 0.6f;
    }

    public float getLength() {
        return 0.6f;
    }

    public void spawn(UpstreamSession session) {
        if (session.isSpawned()) {
            if (this.peType == EntityType.PLAYER) {
                PlayerListEntry playerListEntry = session.getPlayerInfoCache().get(this.playerUniqueId);
                AddPlayerPacket pk = new AddPlayerPacket();
                pk.eid = this.proxyEid;
                pk.rtid = this.proxyEid;
                pk.uuid = this.playerUniqueId;
                pk.position = new Vector3F((float) this.x, (float) this.y, (float) this.z);
                pk.motion = Vector3F.ZERO;
                pk.yaw = this.yaw;
                pk.pitch = this.pitch;
                pk.username = playerListEntry.getProfile().getName();
                pk.meta = EntityMetaTranslator.translateToPE(session, this.pcMeta, this.peType);
                pk.meta.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(playerListEntry.getProfile().getName())); //hacky for now
                this.spawned = true;
                session.sendPacket(pk);
            } else if (this.peType == EntityType.ITEM) {
                AddItemEntityPacket pk = new AddItemEntityPacket();
                pk.rtid = this.proxyEid;
                pk.eid = this.proxyEid;
                pk.metadata = EntityMetaTranslator.translateToPE(session, this.pcMeta, this.peType);
                pk.item = ((SlotMeta) pk.metadata.map.get(EntityMetaData.Constants.DATA_TYPE_SLOT)).slot;
                pk.position = new Vector3F((float) this.x, (float) this.y + this.peType.getOffset(), (float) this.z);
                pk.motion = new Vector3F((float) this.motionX, (float) this.motionY, (float) this.motionZ);
                this.spawned = true;
                session.sendPacket(pk);
            } else if (this.peType == EntityType.PAINTING) {
                AddPaintingPacket pk = new AddPaintingPacket();
                pk.rtid = this.proxyEid;
                pk.eid = this.proxyEid;
                pk.pos = new BlockPosition((int) this.x, (int) this.y, (int) this.z);
                pk.direction = 1;
                pk.title = "Kebab";
                this.spawned = true;
//                session.sendPacket(pk); //BUGGY
            } else if (this.peType != null) {
                AddEntityPacket pk = new AddEntityPacket();
                pk.rtid = this.proxyEid;
                pk.eid = this.proxyEid;
                pk.type = this.peType.getPeType();
                pk.position = new Vector3F((float) this.x, (float) this.y - this.peType.getOffset(), (float) this.z);
                pk.motion = new Vector3F((float) this.motionX, (float) this.motionY, (float) this.motionZ);
                pk.yaw = this.yaw;
                pk.pitch = this.pitch;
                pk.meta = EntityMetaTranslator.translateToPE(session, this.pcMeta, this.peType);
                // TODO: Hack for now. ;P
                pk.attributes = this.attributes.values();
                this.spawned = true;
                session.sendPacket(pk);
            }
            this.updateLinks(session);
            // Process equipments
            this.updateEquipment(session);
        }
    }

    public void updateLinks(UpstreamSession session) {
        if (session.isSpawned())
            if (!this.passengers.isEmpty())
                for (long passenger : this.passengers) {
                    SetEntityLinkPacket pk = new SetEntityLinkPacket();
                    pk.riding = proxyEid;
                    pk.rider = passenger;
                    pk.type = SetEntityLinkPacket.TYPE_RIDE;
                    pk.unknownByte = 0x00;
                    session.sendPacket(pk);
                }
    }

    public void updateEquipment(UpstreamSession session) {
        if (session.isSpawned())
            if (this.helmet != null || this.chestplate != null || this.leggings != null || this.boots != null || this.mainHand != null) {
                MobArmorEquipmentPacket aeq = new MobArmorEquipmentPacket();
                aeq.rtid = this.proxyEid;
                aeq.helmet = this.helmet;
                aeq.chestplate = this.chestplate;
                aeq.leggings = this.leggings;
                aeq.boots = this.boots;
                session.sendPacket(aeq);
            }
    }
}
