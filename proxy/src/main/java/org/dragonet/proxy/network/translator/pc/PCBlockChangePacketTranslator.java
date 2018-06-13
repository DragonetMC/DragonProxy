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
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.api.translators.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;

import org.dragonet.common.data.blocks.GlobalBlockPalette;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.protocol.packets.LevelSoundEventPacket;
import org.dragonet.protocol.packets.PlaySoundPacket;
import org.dragonet.protocol.packets.UpdateBlockPacket;
import org.dragonet.common.maths.BlockPosition;
import org.dragonet.common.maths.Vector3F;

public class PCBlockChangePacketTranslator implements IPCPacketTranslator<ServerBlockChangePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerBlockChangePacket packet) {
        Position pos = packet.getRecord().getPosition();
        BlockState block = packet.getRecord().getBlock();
        if (session.getChunkCache().getBlock(pos) != null) {
            if (block.getId() == 0 && session.getChunkCache().getBlock(pos).getId() != 0) {
                LevelEventPacket pk = new LevelEventPacket();
                pk.eventId = LevelEventPacket.EVENT_PARTICLE_DESTROY;
                pk.position = new Vector3F(pos.getX(), pos.getY(), pos.getZ());
                ItemEntry entry = ItemBlockTranslator.translateToPE(session.getChunkCache().getBlock(pos).getId(),
                        session.getChunkCache().getBlock(pos).getData());
                pk.data = GlobalBlockPalette.getOrCreateRuntimeId(entry.getId(), entry.getPEDamage());
                session.sendPacket(pk);
            } else if (isDoor(block.getId())) {
                if ((block.getData() & 0x4) == 0x4 && (session.getChunkCache().getBlock(pos).getData() & 0x4) != 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator()
                            .translate(block.getId() == 71 ? BuiltinSound.BLOCK_IRON_DOOR_CLOSE
                                    : BuiltinSound.BLOCK_WOODEN_DOOR_OPEN);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                } else if ((block.getData() & 0x4) != 0x4
                        && (session.getChunkCache().getBlock(pos).getData() & 0x4) == 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator()
                            .translate(block.getId() == 71 ? BuiltinSound.BLOCK_IRON_DOOR_CLOSE
                                    : BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                }
            } else if (isGate(block.getId())) {
                if ((block.getData() & 0x4) == 0x4 && (session.getChunkCache().getBlock(pos).getData() & 0x4) != 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator().translate(BuiltinSound.BLOCK_FENCE_GATE_OPEN);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                } else if ((block.getData() & 0x4) != 0x4
                        && (session.getChunkCache().getBlock(pos).getData() & 0x4) == 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator().translate(BuiltinSound.BLOCK_FENCE_GATE_CLOSE);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                }
            } else if (isTrapdoor(block.getId())) {
                if ((block.getData() & 0x4) == 0x4 && (session.getChunkCache().getBlock(pos).getData() & 0x4) != 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator()
                            .translate(block.getId() == 167 ? BuiltinSound.BLOCK_IRON_TRAPDOOR_OPEN
                                    : BuiltinSound.BLOCK_WOODEN_TRAPDOOR_OPEN);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                } else if ((block.getData() & 0x4) != 0x4
                        && (session.getChunkCache().getBlock(pos).getData() & 0x4) == 0x4) {
                    PlaySoundPacket psp = new PlaySoundPacket();
                    psp.blockPosition = new BlockPosition(pos);
                    psp.name = session.getProxy().getSoundTranslator()
                            .translate(block.getId() == 167 ? BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE
                                    : BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE);
                    psp.volume = 1;
                    psp.pitch = 1;
                    session.sendPacket(psp);
                }
            } else if (block.getId() != 0 && session.getChunkCache().getBlock(pos).getId() != block.getId()) {
                build(session, pos, block);
            }
        } else {
            build(session, pos, block);
        }
        // update cache
        try {
            session.getChunkCache().update(pos, block);

            // Save glitchy items in cache
            // Position blockPosition = new Position(pk.blockPosition.x, pk.blockPosition.y,
            // pk.blockPosition.z);
            // session.getBlockCache().checkBlock(entry.getId(), entry.getPEDamage(),
            // blockPosition);
            ItemEntry entry = session.getChunkCache().translateBlock(pos);
            if (entry != null) {
                UpdateBlockPacket pk = new UpdateBlockPacket();
                pk.flags = UpdateBlockPacket.FLAG_NEIGHBORS;
                pk.data = entry.getPEDamage();
                pk.id = entry.getId();
                pk.blockPosition = new BlockPosition(pos);
                session.putCachePacket(pk);
            }
        } catch (Exception ex) {
            session.getProxy().getLogger().debug("Error when updating block [" + pos.getX() + "," + pos.getY() + ","
                    + pos.getZ() + "] " + block.toString());
            session.getProxy().getLogger().debug(ex.getMessage());
        }
        return null;
    }

    public void build(UpstreamSession session, Position pos, BlockState block) {
        LevelSoundEventPacket pk = new LevelSoundEventPacket();
        pk.sound = LevelSoundEventPacket.Sound.PLACE;
        pk.position = new Vector3F(pos.getX(), pos.getY(), pos.getZ());
        ItemEntry entry = ItemBlockTranslator.translateToPE(block.getId(), block.getData());
        pk.extraData = GlobalBlockPalette.getOrCreateRuntimeId(entry.getId(), entry.getPEDamage());
        pk.isGlobal = false;
        pk.pitch = 1;
        session.sendPacket(pk);
    }

    public boolean isDoor(int id) {
        return id == 64 || id == 193 || id == 194 || id == 195 || id == 196 || id == 197 || id == 71;
    }

    public boolean isGate(int id) {
        return id == 107 || id == 183 || id == 184 || id == 185 || id == 186 || id == 187;
    }

    public boolean isTrapdoor(int id) {
        return id == 96 || id == 167;
    }
}
