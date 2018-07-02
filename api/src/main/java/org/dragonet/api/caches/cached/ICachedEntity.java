/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
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
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.api.caches.cached;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.dragonet.api.sessions.IUpstreamSession;
import org.dragonet.common.data.entity.EntityType;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.common.data.inventory.Slot;
import org.dragonet.common.maths.AxisAlignedBB;
import org.dragonet.common.maths.BlockPosition;

/**
 *
 * @author Epic
 */
public interface ICachedEntity {

    /**
     * @return the eid
     */
    public long getEid();

    /**
     * @param eid the eid to set
     */
    public void setEid(long eid);
    /**
     * @return the proxyEid
     */
    public long getProxyEid();

    /**
     * @return the pcType
     */
    public int getPcType();

    /**
     * @return the peType
     */
    public EntityType getPeType();

    /**
     * @return the objType
     */
    public ObjectType getObjType();

    /**
     * @return the player
     */
    public boolean isPlayer();

    /**
     * @return the playerUniqueId
     */
    public UUID getPlayerUniqueId();

    /**
     * @return the dimention
     */
    public int getDimention();

    /**
     * @param dimention the dimention to set
     */
    public void setDimention(int dimention);

    /**
     * @return the x
     */
    public double getX();

    /**
     * @param x the x to set
     */
    public void setX(double x);

    /**
     * @return the y
     */
    public double getY();

    /**
     * @param y the y to set
     */
    public void setY(double y);

    /**
     * @return the z
     */
    public double getZ();

    /**
     * @param z the z to set
     */
    public void setZ(double z);

    /**
     * @return the scale
     */
    public int getScale();

    /**
     * @param scale the scale to set
     */
    public void setScale(int scale);

    /**
     * @return the motionX
     */
    public double getMotionX();

    /**
     * @param motionX the motionX to set
     */
    public void setMotionX(double motionX);

    /**
     * @return the motionY
     */
    public double getMotionY();

    /**
     * @param motionY the motionY to set
     */
    public void setMotionY(double motionY);

    /**
     * @return the motionZ
     */
    public double getMotionZ();

    /**
     * @param motionZ the motionZ to set
     */
    public void setMotionZ(double motionZ);

    /**
     * @return the yaw
     */
    public float getYaw();

    /**
     * @param yaw the yaw to set
     */
    public void setYaw(float yaw);

    /**
     * @return the headYaw
     */
    public float getHeadYaw();

    /**
     * @param headYaw the headYaw to set
     */
    public void setHeadYaw(float headYaw);

    /**
     * @return the pitch
     */
    public float getPitch();

    /**
     * @param pitch the pitch to set
     */
    public void setPitch(float pitch);

    /**
     * @return the shouldMove
     */
    public boolean isShouldMove();

    /**
     * @param shouldMove the shouldMove to set
     */
    public void setShouldMove(boolean shouldMove);

    /**
     * @return the spawnPosition
     */
    public BlockPosition getSpawnPosition();

    /**
     * @param spawnPosition the spawnPosition to set
     */
    public void setSpawnPosition(BlockPosition spawnPosition);

    /**
     * @return the helmet
     */
    public Slot getHelmet();

    /**
     * @param helmet the helmet to set
     */
    public void setHelmet(Slot helmet);

    /**
     * @return the chestplate
     */
    public Slot getChestplate();

    /**
     * @param chestplate the chestplate to set
     */
    public void setChestplate(Slot chestplate);

    /**
     * @return the leggings
     */
    public Slot getLeggings();

    /**
     * @param leggings the leggings to set
     */
    public void setLeggings(Slot leggings);

    /**
     * @return the boots
     */
    public Slot getBoots();

    /**
     * @param boots the boots to set
     */
    public void setBoots(Slot boots);

    /**
     * @return the mainHand
     */
    public Slot getMainHand();

    /**
     * @param mainHand the mainHand to set
     */
    public void setMainHand(Slot mainHand);

    /**
     * @return the foodPacketCount
     */
    public int getFoodPacketCount();

    /**
     * @param foodPacketCount the foodPacketCount to set
     */
    public void setFoodPacketCount(int foodPacketCount);

    /**
     * @return the lastFoodPacketTime
     */
    public long getLastFoodPacketTime();

    /**
     * @param lastFoodPacketTime the lastFoodPacketTime to set
     */
    public void setLastFoodPacketTime(long lastFoodPacketTime);

    /**
     * @return the isOnStairs
     */
    public boolean isIsOnStairs();

    /**
     * @param isOnStairs the isOnStairs to set
     */
    public void setIsOnStairs(boolean isOnStairs);

    /**
     * @return the boundingBox
     */
    public AxisAlignedBB getBoundingBox();

    /**
     * @param boundingBox the boundingBox to set
     */
    public void setBoundingBox(AxisAlignedBB boundingBox);

    /**
     * @return the riding
     */
    public long getRiding();

    /**
     * @param riding the riding to set
     */
    public void setRiding(long riding);

    /**
     * @return the passengers
     */
    public Set<Long> getPassengers();

    /**
     * @param passengers the passengers to set
     */
    public void setPassengers(Set<Long> passengers);

    /**
     * @return the pcMeta
     */
    public EntityMetadata[] getPcMeta();

    /**
     * @param pcMeta the pcMeta to set
     */
    public void setPcMeta(EntityMetadata[] pcMeta) ;

    /**
     * @return the spawned
     */
    public boolean isSpawned();

    /**
     * @param spawned the spawned to set
     */
    public void setSpawned(boolean spawned);

    /**
     * @return the effects
     */
    public Set<Integer> getEffects();

    /**
     * @return the attributes
     */
    public Map<Integer, PEEntityAttribute> getAttributes();

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<Integer, PEEntityAttribute> attributes);

    public ICachedEntity relativeMove(double rx, double ry, double rz, float yaw, float pitch);

    public ICachedEntity absoluteMove(double x, double y, double z, float yaw, float pitch);

    public ICachedEntity relativeMove(double rx, double ry, double rz);

    public ICachedEntity relativeMove(float yaw, float pitch);

    public float getHeight();

    public float getWidth();

    public float getLength();

    public void spawn(IUpstreamSession session);

    public void despawn(IUpstreamSession session);

    public void updateLinks(IUpstreamSession session);

    public void updateEquipment(IUpstreamSession session);
}
