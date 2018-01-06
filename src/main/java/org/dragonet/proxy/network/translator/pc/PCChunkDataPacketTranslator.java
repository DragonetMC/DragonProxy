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

import com.github.steveice10.mc.protocol.data.game.chunk.BlockStorage;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.dragonet.common.mcbedrock.data.nbt.NBTIO;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.common.mcbedrock.blocks.Block;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.common.mcbedrock.data.itemsblocks.ItemEntry;
import org.dragonet.common.mcbedrock.data.nbt.tag.CompoundTag;
import org.dragonet.common.mcbedrock.protocol.PEPacket;
import org.dragonet.common.mcbedrock.protocol.packets.FullChunkDataPacket;
import org.dragonet.common.mcbedrock.protocol.type.chunk.ChunkData;
import org.dragonet.common.mcbedrock.protocol.type.chunk.Section;
import org.dragonet.proxy.utilities.Position;


public class PCChunkDataPacketTranslator implements IPCPacketTranslator<ServerChunkDataPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerChunkDataPacket packet) {
        session.getProxy().getGeneralThreadPool().execute(() -> {
            try {

                FullChunkDataPacket pePacket = new FullChunkDataPacket();
                pePacket.x = packet.getColumn().getX();
                pePacket.z = packet.getColumn().getZ();

                ChunkData chunk = new ChunkData();
                processChunkSection(packet.getColumn(), chunk, session);
                chunk.encode();
                pePacket.payload = chunk.getBuffer();

                session.putCachePacket(pePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    private void processChunkSection(Column pc, ChunkData pe, UpstreamSession session) {
        /*
         * pe.sections = new Section[16]; for(int i = 0; i < 16; i++) { pe.sections[i] =
         * new Section(); if(i < 2) Arrays.fill(pe.sections[i].blockIds, (byte)1); }
         */
        pe.sections = new Section[16];
        for (int i = 0; i < 16; i++) {
            pe.sections[i] = new Section();
        }
        // Blocks
        for (int y = 0; y < 256; y++) {
            int cy = y >> 4;

            Chunk c = pc.getChunks()[cy];
            if (c == null || c.isEmpty()) {
                continue;
            }
            BlockStorage blocks = c.getBlocks();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockState block = blocks.get(x, y & 0xF, z);
                    ItemEntry entry = ItemBlockTranslator.translateToPE(block.getId(), block.getData());

                    Section section = pe.sections[cy];
                    //Block id
                    section.blockIds[index(x, y, z)] = (byte) (entry.getId() & 0xFF);

                    //Data value
                    int i = dataIndex(x, y, z);
                    byte data = section.blockMetas[i];
                    int newValue = entry.getPEDamage().byteValue();

                    if ((y & 1) == 0) {
                        data = (byte) ((data & 0xf0) | (newValue & 0x0f));
                    } else {
                        data = (byte) (((newValue & 0x0f) << 4) | (data & 0x0f));
                    }

                    // Save glitchy items in cache
                    Block glitchyBlock = Block.get(entry.getId(), entry.getPEDamage(), getCoordinateFromChunkData(pc.getX(), pc.getZ(), x, y, z));
                    if (glitchyBlock != null) {
                        DragonProxy.getInstance().getLogger().debug("Added glitchy block : " + glitchyBlock);
                        session.getBlockCache().saveBlock(glitchyBlock);
                    }

                    section.blockMetas[i] = data;
                }
            }
        }
        // Blocks entities
        try {
            List<CompoundTag> blockEntities = new ArrayList<>();
            for (int i = 0; i < pc.getTileEntities().length; i++) {
                CompoundTag peTag = ItemBlockTranslator.translateBlockEntityToPE(pc.getTileEntities()[i]);
                if (peTag != null) //filter non hadled blocks entities
                    blockEntities.add(peTag);
//                else // debug
//                    DragonProxy.getInstance().getLogger().debug("NBT null for " + pc.getTileEntities()[i].toString());
            }
            pe.blockEntities = NBTIO.write(blockEntities, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static int index(int x, int y, int z) {
        return (x << 8) | (z << 4) | (y & 0xF);
    }

    private static int dataIndex(int x, int y, int z) {
        return (x << 7) | (z << 3) | ((y & 0xF) >> 1);
    }

    private Position getCoordinateFromChunkData(int chunkX, int chunkZ, int x, int y, int z) {
        return new Position(chunkX*16 + (x), y, chunkZ*16 + z);
    }
}

