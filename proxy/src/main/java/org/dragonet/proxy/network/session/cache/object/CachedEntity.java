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
package org.dragonet.proxy.network.session.cache.object;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.packet.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttributeType;
import org.dragonet.proxy.data.entity.BedrockEntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.misc.EntityEffectTranslator;

import java.util.*;

@Getter
@Setter
@Log4j2
public class CachedEntity {
    protected BedrockEntityType entityType;
    protected long proxyEid;
    protected int remoteEid; // will be -1 if its a local entity
    protected UUID javaUuid;

    protected EntityMetadata[] remoteMetadata;
    protected EntityDataMap metadata = new EntityDataMap();

    protected boolean spawned = false;

    protected Vector3f position = Vector3f.ZERO;
    protected Vector3f rotation = Vector3f.ZERO;
    protected Vector3f motion = Vector3f.ZERO;
    protected Vector3f spawnPosition = Vector3f.ZERO;

    protected ItemData helmet = ItemData.AIR;
    protected ItemData chestplate = ItemData.AIR;
    protected ItemData leggings = ItemData.AIR;
    protected ItemData boots = ItemData.AIR;

    protected ItemData mainHand = ItemData.AIR;
    protected ItemData offHand = ItemData.AIR;

    // HACK for invisiblity, as we need to access the original scale
    private float scale = 0;

    protected int dimension = 0; // -1 = nether, 0 = overworld, 1 = end

    protected Map<BedrockAttributeType, Attribute> attributes = new HashMap<>();
    protected Set<EntityEffectTranslator.BedrockEffect> effects = new ObjectOpenHashSet<>();
    protected Set<CachedEntity> passengers = new ObjectOpenHashSet<>();

    // This feels like an ugly way to do it, but here it is
    protected Map<String, Object> extraData = new HashMap<>();

    protected CachedEntity riding;

    @Setter(AccessLevel.PRIVATE)
    private boolean local = false;

    public CachedEntity(BedrockEntityType entityType, long proxyEid, int remoteEid) {
        this.entityType = entityType;
        this.proxyEid = proxyEid;
        this.remoteEid = remoteEid;

        if(entityType != null) {
            addDefaultData();
        }
    }

    public CachedEntity(BedrockEntityType entityType, long proxyEid) {
        this.entityType = entityType;
        this.proxyEid = proxyEid;
        this.local = true;

        if(entityType != null) {
            addDefaultData();
        }
    }

    public void spawn(ProxySession session) {
        if(spawned) {
            throw new IllegalStateException("Cannot spawn entity that is already spawned");
        }

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setRuntimeEntityId(proxyEid);
        addEntityPacket.setUniqueEntityId(proxyEid);
        addEntityPacket.setIdentifier("minecraft:" + entityType.name().toLowerCase()); // TODO: this may need mapping
        addEntityPacket.setEntityType(0);
        addEntityPacket.setRotation(rotation);
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setPosition(position);

        addEntityPacket.getMetadata().putAll(metadata);
        addEntityPacket.getAttributes().addAll(attributes.values());

        //log.info(getMetadata());

        session.sendPacket(addEntityPacket);
        spawned = true;
    }

    public void despawn(ProxySession session) {
        if(spawned) {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setUniqueEntityId(proxyEid);

            session.sendPacket(removeEntityPacket);
            spawned = false;
        }
    }

    public void destroy(ProxySession session) {
        despawn(session);
        session.getEntityCache().destroyEntity(proxyEid);
    }

    public void moveRelative(ProxySession session, Vector3f relPos, Vector3f rotation, boolean onGround, boolean teleported) {
        if (relPos.getX() == 0 && relPos.getY() == 0 && relPos.getZ() == 0 && position.getX() == 0 && position.getY() == 0)
            return;

        this.position = Vector3f.from(position.getX() + relPos.getX(), position.getY() + relPos.getY(), position.getZ() + relPos.getZ());
        this.rotation = rotation;

        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(proxyEid);
        moveEntityPacket.setPosition(position);
        moveEntityPacket.setRotation(rotation);
        moveEntityPacket.setTeleported(teleported);
        moveEntityPacket.setOnGround(onGround);

        session.sendPacket(moveEntityPacket);
    }

    public void moveAbsolute(ProxySession session, Vector3f position, Vector3f rotation, boolean onGround, boolean teleported) {
        if (position.getX() == 0 && position.getY() == 0 && position.getZ() == 0 && rotation.getX() == 0 && rotation.getY() == 0)
            return;

        this.position = position;
        this.rotation = rotation;

        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(proxyEid);
        moveEntityPacket.setPosition(position);
        moveEntityPacket.setRotation(rotation);
        moveEntityPacket.setTeleported(teleported);
        moveEntityPacket.setOnGround(onGround);

        session.sendPacket(moveEntityPacket);
    }

    public void rotate(ProxySession session, Vector3f rotation) {
        this.rotation = rotation;

        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(proxyEid);
        moveEntityPacket.setPosition(position);
        moveEntityPacket.setRotation(rotation);
        moveEntityPacket.setTeleported(false);

        session.sendPacket(moveEntityPacket);
    }

    public void sendArmor(ProxySession session) {
        MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
        mobArmorEquipmentPacket.setRuntimeEntityId(proxyEid);
        mobArmorEquipmentPacket.setHelmet(helmet);
        mobArmorEquipmentPacket.setChestplate(chestplate);
        mobArmorEquipmentPacket.setLeggings(leggings);
        mobArmorEquipmentPacket.setBoots(boots);

        session.sendPacket(mobArmorEquipmentPacket);
    }

    public void sendAttributes(ProxySession session) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(proxyEid);
        updateAttributesPacket.setAttributes(new ArrayList<>(attributes.values()));

        session.sendPacket(updateAttributesPacket);
    }

    public void sendMetadata(ProxySession session) {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.setRuntimeEntityId(proxyEid);
        setEntityDataPacket.getMetadata().putAll(metadata);

        session.sendPacket(setEntityDataPacket);
    }

    public boolean setEntityFlag(EntityFlag flag, boolean value) {
        return metadata.getFlags().setFlag(flag, value);
    }

    public void onTick(ProxySession session) {

    }

    private void addDefaultData() {
        scale = 1f;

        // Metadata
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.AIR, (short) 300);
        metadata.put(EntityData.MAX_AIR, (short) 300);
        metadata.put(EntityData.BOUNDING_BOX_HEIGHT, entityType.getHeight());
        metadata.put(EntityData.BOUNDING_BOX_WIDTH, entityType.getWidth());
        metadata.put(EntityData.LEAD_HOLDER_EID, (long) -1);

        // Metadata flags
        EntityFlags flags = new EntityFlags();
        flags.setFlag(EntityFlag.HAS_GRAVITY, true);
        flags.setFlag(EntityFlag.HAS_COLLISION, true);
        flags.setFlag(EntityFlag.CAN_SHOW_NAME, true);
        flags.setFlag(EntityFlag.CAN_CLIMB, true);
        flags.setFlag(EntityFlag.CAN_WALK, true);
        flags.setFlag(EntityFlag.BREATHING, true);
        flags.setFlag(EntityFlag.WALL_CLIMBING, false);

        // Add the flags to the metadata
        metadata.putFlags(flags);

        // Attributes
        attributes.put(BedrockAttributeType.MOVEMENT_SPEED, BedrockAttributeType.MOVEMENT_SPEED.createDefault());

        // TODO: move this out of here
        if(entityType == BedrockEntityType.ENDER_DRAGON) {
            attributes.put(BedrockAttributeType.HEALTH, BedrockAttributeType.HEALTH.create(200f, 200f));
        }
    }

    public Vector3f getOffsetPosition() {
        return Vector3f.from(position.getX(), position.getY() + entityType.getOffset(), position.getZ());
    }
}
