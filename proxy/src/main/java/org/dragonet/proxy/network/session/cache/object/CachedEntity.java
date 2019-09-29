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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache.object;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.nukkitx.protocol.PlayerSession;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import lombok.Data;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CachedEntity {
    protected EntityType type;
    protected long proxyEid;
    protected long remoteEid;
    protected UUID javaUuid;

    protected EntityDataDictionary metadata = new EntityDataDictionary();
    protected EntityFlags flags = new EntityFlags();

    protected boolean spawned = false;
    protected boolean shouldMove = false;

    protected Vector3f position = Vector3f.ZERO;
    protected Vector3f rotation = Vector3f.ZERO;
    protected Vector3f motion = Vector3f.ZERO;
    protected Vector3f spawnPosition = Vector3f.ZERO;

    protected Set<EntityEffectTranslator.BedrockEffect> effects = new HashSet<>();

    public CachedEntity(EntityType type, long proxyEid, int remoteEid) {
        this.type = type;
        this.proxyEid = proxyEid;
        this.remoteEid = remoteEid;

        addDefaultMetadata();
    }

    public void spawn(ProxySession session) {
        if(spawned) {
            throw new IllegalStateException("Cannot spawn entity that is already spawned");
        }

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setIdentifier("minecraft:" + type.name().toLowerCase()); // TODO: this may need mapping
        addEntityPacket.setEntityType(type.getType());
        addEntityPacket.setRotation(rotation);
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setPosition(position);
        addEntityPacket.setRuntimeEntityId(proxyEid);
        addEntityPacket.setUniqueEntityId(proxyEid);
        addEntityPacket.getMetadata().putAll(getMetadata());

        session.getBedrockSession().sendPacket(addEntityPacket);
        spawned = true;

        session.getEntityCache().getEntities().put(proxyEid, this);
    }

    public void despawn(ProxySession session) {
        if(spawned) {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setUniqueEntityId(proxyEid);

            session.getBedrockSession().sendPacket(removeEntityPacket);
            spawned = false;
        }
    }

    public void destroy(ProxySession session) {
        despawn(session);
        session.getEntityCache().destroyEntity(proxyEid);
    }

    public void moveRelative(Vector3f relPos, float pitch, float yaw) {
        moveRelative(relPos, new Vector3f(pitch, yaw, 0));
    }

    public void moveRelative(Vector3f relPos, Vector3f rotation) {
        if (relPos.getX() == 0 && relPos.getY() == 0 && relPos.getZ() == 0 && position.getX() == 0 && position.getY() == 0)
            return;

        this.rotation = rotation;
        this.position = new Vector3f(position.getX() + relPos.getX(), position.getY() + relPos.getY(), position.getZ() + relPos.getZ());
        this.shouldMove = true;
    }

    public void moveAbsolute(Vector3f position, float pitch, float yaw) {
        moveAbsolute(position, new Vector3f(pitch, yaw, yaw));
    }

    public void moveAbsolute(Vector3f position, Vector3f rotation) {
        if (position.getX() == 0 && position.getY() == 0 && position.getZ() == 0 && rotation.getX() == 0 && rotation.getY() == 0)
            return;

        this.position = position;
        this.rotation = rotation;
        this.shouldMove = true;
    }

    private void addDefaultMetadata() {
        flags.setFlag(EntityFlag.HAS_GRAVITY, true);
        flags.setFlag(EntityFlag.HAS_COLLISION, true);
        flags.setFlag(EntityFlag.CAN_SHOW_NAME, true);
        flags.setFlag(EntityFlag.CAN_CLIMB, true);
        flags.setFlag(EntityFlag.NO_AI, false);

        //metadata.put(EntityData.NAMETAG, "test - " + proxyEid);
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.AIR, 0);
        metadata.put(EntityData.MAX_AIR, 400);
        metadata.put(EntityData.ENTITY_AGE, 0);
        metadata.put(EntityData.BOUNDING_BOX_HEIGHT, (float) type.getHeight());
        metadata.put(EntityData.BOUNDING_BOX_WIDTH, (float) type.getWidth());
        metadata.putFlags(flags);
    }
}
