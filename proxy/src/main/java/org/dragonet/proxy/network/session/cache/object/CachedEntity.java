/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
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

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.packet.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dragonet.proxy.data.entity.BedrockAttributeType;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

import java.util.*;

@Data
@Log4j2
public class CachedEntity {
    protected EntityType entityType;
    protected long proxyEid;
    protected int remoteEid; // will be -1 if its a local entity
    protected UUID javaUuid;

    protected EntityDataMap metadata = new EntityDataMap();
    protected EntityFlags flags = new EntityFlags();

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
    protected Set<EntityEffectTranslator.BedrockEffect> effects = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    private boolean local = false;

    public CachedEntity(EntityType entityType, long proxyEid, int remoteEid) {
        this.entityType = entityType;
        this.proxyEid = proxyEid;
        this.remoteEid = remoteEid;

        addDefaultMetadata();
    }

    public CachedEntity(EntityType entityType, long proxyEid) {
        this.entityType = entityType;
        this.proxyEid = proxyEid;
        this.local = true;

        addDefaultMetadata();
    }

    public void spawn(ProxySession session) {
        if(spawned) {
            throw new IllegalStateException("Cannot spawn entity that is already spawned");
        }

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setRuntimeEntityId(proxyEid);
        addEntityPacket.setUniqueEntityId(proxyEid);
        addEntityPacket.setIdentifier("minecraft:" + entityType.name().toLowerCase()); // TODO: this may need mapping
        addEntityPacket.setEntityType(entityType.getType());
        addEntityPacket.setRotation(rotation);
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setPosition(getOffsetPosition());
        addEntityPacket.getMetadata().putAll(getMetadata());

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
        moveEntityPacket.setPosition(getOffsetPosition());
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
        moveEntityPacket.setPosition(getOffsetPosition());
        moveEntityPacket.setRotation(rotation);
        moveEntityPacket.setTeleported(teleported);
        moveEntityPacket.setOnGround(onGround);

        session.sendPacket(moveEntityPacket);
    }

    public void rotate(ProxySession session, Vector3f rotation) {
        this.rotation = rotation;

        MoveEntityAbsolutePacket moveEntityPacket = new MoveEntityAbsolutePacket();
        moveEntityPacket.setRuntimeEntityId(proxyEid);
        moveEntityPacket.setPosition(getOffsetPosition());
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
        setEntityDataPacket.getMetadata().putAll(metadata.putFlags(flags));

        session.sendPacket(setEntityDataPacket);
    }

    private void addDefaultMetadata() {
        flags.setFlag(EntityFlag.HAS_GRAVITY, true);
        flags.setFlag(EntityFlag.HAS_COLLISION, true);
        flags.setFlag(EntityFlag.CAN_SHOW_NAME, true);
        flags.setFlag(EntityFlag.CAN_CLIMB, true);

        scale = 1f;
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.AIR, (short) 0);
        metadata.put(EntityData.MAX_AIR, (short) 400);
        metadata.put(EntityData.BOUNDING_BOX_HEIGHT, entityType.getHeight());
        metadata.put(EntityData.BOUNDING_BOX_WIDTH, entityType.getWidth());
        metadata.putFlags(flags);
    }

    public Vector3f getOffsetPosition() {
        return Vector3f.from(position.getX(), position.getY() + entityType.getOffset(), position.getZ());
    }
}
