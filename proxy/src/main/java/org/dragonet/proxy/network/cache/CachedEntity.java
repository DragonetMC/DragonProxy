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
import java.util.HashMap;
import java.util.Map;

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;

import org.dragonet.common.data.entity.EntityType;
import org.dragonet.common.maths.AxisAlignedBB;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.common.data.inventory.Slot;

import org.dragonet.api.caches.cached.ICachedEntity;
import org.dragonet.api.sessions.IUpstreamSession;

import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.common.data.entity.meta.EntityMetaData;
import org.dragonet.common.data.entity.meta.type.ByteArrayMeta;
import org.dragonet.common.data.entity.meta.type.SlotMeta;
import org.dragonet.proxy.network.translator.EntityMetaTranslator;
import org.dragonet.protocol.packets.*;
import org.dragonet.common.maths.BlockPosition;

public class CachedEntity implements ICachedEntity {

    private long eid;
    private final long proxyEid;
    private final int pcType;
    private final EntityType peType;
    private final ObjectType objType;

    private final boolean player;
    private final UUID playerUniqueId;

    private int dimention;
    private double x;
    private double y;
    private double z;
    private int scale = 1;
    private double motionX;
    private double motionY;
    private double motionZ;
    private float yaw;
    private float headYaw;
    private float pitch;

    private boolean shouldMove = false;
    private BlockPosition spawnPosition;

    private Slot helmet;
    private Slot chestplate;
    private Slot leggings;
    private Slot boots;
    private Slot mainHand;

    private int foodPacketCount = 0;
    private long lastFoodPacketTime;

    private boolean isOnStairs = false;
    private AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    // cache riding datas for dismount
    private long riding = 0;
    private Set<Long> passengers = new HashSet();

    private EntityMetadata[] pcMeta;
    private boolean spawned = false;
    private final Set<Integer> effects = Collections.synchronizedSet(new HashSet<Integer>());
    private Map<Integer, PEEntityAttribute> attributes = Collections.synchronizedMap(new HashMap());

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

    @Override
    public ICachedEntity relativeMove(double rx, double ry, double rz, float yaw, float pitch) {
        if (rx != 0 || ry != 0 || rz != 0 || yaw != 0 || pitch != 0) {
            this.setX(this.getX() + rx);
            this.setY(this.getY() + ry);
            this.setZ(this.getZ() + rz);
            if (yaw != 0)
                this.setYaw(yaw);
            if (pitch != 0)
                this.setPitch(pitch);
            this.setShouldMove(true);
        }
        return this;
    }

    @Override
    public ICachedEntity absoluteMove(double x, double y, double z, float yaw, float pitch) {
        double radius = this.getWidth() / 2d;
        if (x != 0 || y != 0 || z != 0 || yaw != 0 || pitch != 0) {
            this.setX(x);
            this.setY(y);
            this.setZ(z);
            this.setYaw(yaw);
            this.setPitch(pitch);
            this.setShouldMove(true);
            this.getBoundingBox().setBounds(x - radius, y, z - radius, x + radius, y + (this.getHeight() * this.getScale()), z + radius);
        }
        return this;
    }

    @Override
    public CachedEntity relativeMove(double rx, double ry, double rz) {
        this.relativeMove(rx, ry, rz, 0, 0);
        return this;
    }

    @Override
    public CachedEntity relativeMove(float yaw, float pitch) {
        this.relativeMove(yaw, pitch);
        return this;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getLength() {
        return 0.6f;
    }

    @Override
    public void spawn(IUpstreamSession session) {
        if (this.getPeType() == EntityType.PLAYER) {
            PlayerListEntry playerListEntry = (PlayerListEntry)session.getPlayerInfoCache().get(this.getPlayerUniqueId());
            AddPlayerPacket pk = new AddPlayerPacket();
            pk.eid = this.getProxyEid();
            pk.rtid = this.getProxyEid();
            pk.uuid = this.getPlayerUniqueId();
            pk.position = new Vector3F((float) this.getX(), (float) this.getY(), (float) this.getZ());
            pk.motion = Vector3F.ZERO;
            pk.yaw = this.getYaw();
            pk.pitch = this.getPitch();
            pk.username = playerListEntry.getProfile().getName();
            pk.meta = EntityMetaTranslator.translateToPE(session, this.getPcMeta(), this.getPeType());
            pk.meta.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta(playerListEntry.getProfile().getName())); //hacky for now
            this.setSpawned(true);
            session.sendPacket(pk);
        } else if (this.getPeType() == EntityType.ITEM) {
            AddItemEntityPacket pk = new AddItemEntityPacket();
            pk.rtid = this.getProxyEid();
            pk.eid = this.getProxyEid();
            pk.metadata = EntityMetaTranslator.translateToPE(session, this.getPcMeta(), this.getPeType());
            pk.item = ((SlotMeta) pk.metadata.map.get(EntityMetaData.Constants.DATA_TYPE_SLOT)).slot;
            pk.position = new Vector3F((float) this.getX(), (float) this.getY() + this.getPeType().getOffset(), (float) this.getZ());
            pk.motion = new Vector3F((float) this.getMotionX(), (float) this.getMotionY(), (float) this.getMotionZ());
            this.setSpawned(true);
            session.sendPacket(pk);
        } else if (this.getPeType() == EntityType.PAINTING) {
            AddPaintingPacket pk = new AddPaintingPacket();
            pk.rtid = this.getProxyEid();
            pk.eid = this.getProxyEid();
            pk.pos = new BlockPosition((int) this.getX(), (int) this.getY(), (int) this.getZ());
            pk.direction = 1;
            pk.title = "Kebab";
            this.setSpawned(true);
//                session.sendPacket(pk); //BUGGY
        } else if (this.getPeType() != null) {
            AddEntityPacket pk = new AddEntityPacket();
            pk.rtid = this.getProxyEid();
            pk.eid = this.getProxyEid();
            pk.type = this.getPeType().getPeType();
            pk.position = new Vector3F((float) this.getX(), (float) this.getY() - this.getPeType().getOffset(), (float) this.getZ());
            pk.motion = new Vector3F((float) this.getMotionX(), (float) this.getMotionY(), (float) this.getMotionZ());
            pk.yaw = this.getYaw();
            pk.pitch = this.getPitch();
            pk.meta = EntityMetaTranslator.translateToPE(session, this.getPcMeta(), this.getPeType());
            // TODO: Hack for now. ;P
            pk.attributes = this.getAttributes().values();
            this.setSpawned(true);
            session.sendPacket(pk);
        }
        this.updateLinks(session);
        // Process equipments
        this.updateEquipment(session);
    }

    @Override
    public void despawn(IUpstreamSession session) {
        if (session.isSpawned())
            if (isSpawned())
                session.sendPacket(new RemoveEntityPacket(this.getProxyEid()));
    }

    @Override
    public void updateLinks(IUpstreamSession session) {
        if (!this.passengers.isEmpty())
            for (long passenger : this.getPassengers()) {
                SetEntityLinkPacket pk = new SetEntityLinkPacket();
                pk.riding = getProxyEid();
                pk.rider = passenger;
                pk.type = SetEntityLinkPacket.TYPE_RIDE;
                pk.unknownByte = 0x00;
                session.sendPacket(pk);
            }
    }

    @Override
    public void updateEquipment(IUpstreamSession session) {
        if (this.getHelmet() != null || this.getChestplate() != null || this.getLeggings() != null || this.getBoots() != null || this.getMainHand() != null) {
            MobArmorEquipmentPacket aeq = new MobArmorEquipmentPacket();
            aeq.rtid = this.getProxyEid();
            aeq.helmet = this.getHelmet();
            aeq.chestplate = this.getChestplate();
            aeq.leggings = this.getLeggings();
            aeq.boots = this.getBoots();
            session.sendPacket(aeq);
        }
    }

    /**
     * @return the eid
     */
    public long getEid() {
        return eid;
    }

    /**
     * @param eid the eid to set
     */
    public void setEid(long eid) {
        this.eid = eid;
    }

    /**
     * @return the proxyEid
     */
    public long getProxyEid() {
        return proxyEid;
    }

    /**
     * @return the pcType
     */
    public int getPcType() {
        return pcType;
    }

    /**
     * @return the peType
     */
    public EntityType getPeType() {
        return peType;
    }

    /**
     * @return the objType
     */
    public ObjectType getObjType() {
        return objType;
    }

    /**
     * @return the player
     */
    public boolean isPlayer() {
        return player;
    }

    /**
     * @return the playerUniqueId
     */
    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    /**
     * @return the dimention
     */
    public int getDimention() {
        return dimention;
    }

    /**
     * @param dimention the dimention to set
     */
    public void setDimention(int dimention) {
        this.dimention = dimention;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * @return the motionX
     */
    public double getMotionX() {
        return motionX;
    }

    /**
     * @param motionX the motionX to set
     */
    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    /**
     * @return the motionY
     */
    public double getMotionY() {
        return motionY;
    }

    /**
     * @param motionY the motionY to set
     */
    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    /**
     * @return the motionZ
     */
    public double getMotionZ() {
        return motionZ;
    }

    /**
     * @param motionZ the motionZ to set
     */
    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    /**
     * @return the yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * @param yaw the yaw to set
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * @return the headYaw
     */
    public float getHeadYaw() {
        return headYaw;
    }

    /**
     * @param headYaw the headYaw to set
     */
    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    /**
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * @param pitch the pitch to set
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * @return the shouldMove
     */
    public boolean isShouldMove() {
        return shouldMove;
    }

    /**
     * @param shouldMove the shouldMove to set
     */
    public void setShouldMove(boolean shouldMove) {
        this.shouldMove = shouldMove;
    }

    /**
     * @return the spawnPosition
     */
    public BlockPosition getSpawnPosition() {
        return spawnPosition;
    }

    /**
     * @param spawnPosition the spawnPosition to set
     */
    public void setSpawnPosition(BlockPosition spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    /**
     * @return the helmet
     */
    public Slot getHelmet() {
        return helmet;
    }

    /**
     * @param helmet the helmet to set
     */
    public void setHelmet(Slot helmet) {
        this.helmet = helmet;
    }

    /**
     * @return the chestplate
     */
    public Slot getChestplate() {
        return chestplate;
    }

    /**
     * @param chestplate the chestplate to set
     */
    public void setChestplate(Slot chestplate) {
        this.chestplate = chestplate;
    }

    /**
     * @return the leggings
     */
    public Slot getLeggings() {
        return leggings;
    }

    /**
     * @param leggings the leggings to set
     */
    public void setLeggings(Slot leggings) {
        this.leggings = leggings;
    }

    /**
     * @return the boots
     */
    public Slot getBoots() {
        return boots;
    }

    /**
     * @param boots the boots to set
     */
    public void setBoots(Slot boots) {
        this.boots = boots;
    }

    /**
     * @return the mainHand
     */
    public Slot getMainHand() {
        return mainHand;
    }

    /**
     * @param mainHand the mainHand to set
     */
    public void setMainHand(Slot mainHand) {
        this.mainHand = mainHand;
    }

    /**
     * @return the foodPacketCount
     */
    public int getFoodPacketCount() {
        return foodPacketCount;
    }

    /**
     * @param foodPacketCount the foodPacketCount to set
     */
    public void setFoodPacketCount(int foodPacketCount) {
        this.foodPacketCount = foodPacketCount;
    }

    /**
     * @return the lastFoodPacketTime
     */
    public long getLastFoodPacketTime() {
        return lastFoodPacketTime;
    }

    /**
     * @param lastFoodPacketTime the lastFoodPacketTime to set
     */
    public void setLastFoodPacketTime(long lastFoodPacketTime) {
        this.lastFoodPacketTime = lastFoodPacketTime;
    }

    /**
     * @return the isOnStairs
     */
    public boolean isIsOnStairs() {
        return isOnStairs;
    }

    /**
     * @param isOnStairs the isOnStairs to set
     */
    public void setIsOnStairs(boolean isOnStairs) {
        this.isOnStairs = isOnStairs;
    }

    /**
     * @return the boundingBox
     */
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox the boundingBox to set
     */
    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * @return the riding
     */
    public long getRiding() {
        return riding;
    }

    /**
     * @param riding the riding to set
     */
    public void setRiding(long riding) {
        this.riding = riding;
    }

    /**
     * @return the passengers
     */
    public Set<Long> getPassengers() {
        return passengers;
    }

    /**
     * @param passengers the passengers to set
     */
    public void setPassengers(Set<Long> passengers) {
        this.passengers = passengers;
    }

    /**
     * @return the pcMeta
     */
    public EntityMetadata[] getPcMeta() {
        return pcMeta;
    }

    /**
     * @param pcMeta the pcMeta to set
     */
    public void setPcMeta(EntityMetadata[] pcMeta) {
        this.pcMeta = pcMeta;
    }

    /**
     * @return the spawned
     */
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * @param spawned the spawned to set
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * @return the effects
     */
    public Set<Integer> getEffects() {
        return effects;
    }

    /**
     * @return the attributes
     */
    public Map<Integer, PEEntityAttribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<Integer, PEEntityAttribute> attributes) {
        this.attributes = attributes;
    }
}
