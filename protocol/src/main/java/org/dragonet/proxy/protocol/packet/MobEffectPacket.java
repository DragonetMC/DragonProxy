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
package org.dragonet.proxy.protocol.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.PocketPotionEffect;
import org.dragonet.proxy.protocol.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class MobEffectPacket extends PEPacket {

    public enum EffectAction {

        ADD(1),
        MODIFY(2),
        REMOVE(3);

        private int id;

        EffectAction(int id) {
            this.id = id;
        }

        public byte getActionID() {
            return (byte) (id & 0xFF);
        }
    }

    public long eid;
    public EffectAction action;
    public PocketPotionEffect effect;

    @Override
    public int pid() {
        return PEPacketIDs.MOB_EFFECT_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(eid);
            writer.writeByte(action.getActionID());
            writer.writeByte((byte) (effect.getEffect() & 0xFF));
            writer.writeByte((byte) (effect.getAmpilifier() & 0xFF));
            writer.writeByte((byte) (effect.isParticles() ? 1 : 0));
            writer.writeInt(effect.getDuration());
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
