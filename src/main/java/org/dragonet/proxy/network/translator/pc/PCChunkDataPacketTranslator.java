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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dragonet.proxy.data.nbt.NBTIO;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.data.itemsblocks.ItemEntry;
import org.dragonet.proxy.data.nbt.tag.CompoundTag;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.BlockEntityDataPacket;
import org.dragonet.proxy.protocol.packets.FullChunkDataPacket;
import org.dragonet.proxy.protocol.type.chunk.ChunkData;
import org.dragonet.proxy.protocol.type.chunk.Section;
import org.dragonet.proxy.utilities.BlockPosition;

public class PCChunkDataPacketTranslator implements IPCPacketTranslator<ServerChunkDataPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerChunkDataPacket packet) {
        session.getProxy().getGeneralThreadPool().execute(() -> {
            try {

                FullChunkDataPacket pePacket = new FullChunkDataPacket();
                pePacket.x = packet.getColumn().getX();
                pePacket.z = packet.getColumn().getZ();

                ChunkData chunk = new ChunkData();
                processChunkSection(packet.getColumn(), chunk);
                chunk.encode();
                pePacket.payload = chunk.getBuffer();

                session.putCachePacket(pePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    private void processChunkSection(Column pc, ChunkData pe) {
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

                    section.blockMetas[i] = data;
                }
            }
        }
        // Blocks entities (Not Working)
//        try {
//            pe.blockEntities = new byte[pc.getTileEntities().length];
//            for (int i = 0; i < pc.getTileEntities().length; i++) {
//                pe.blockEntities = NBTIO.write(ItemBlockTranslator.translateBlockEntityToPE(pc.getTileEntities()[i]), ByteOrder.LITTLE_ENDIAN, true);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(PCChunkDataPacketTranslator.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        //  This way looks better but client crash
//        try {
//            pe.blockEntities = new byte[pc.getTileEntities().length];
//            List<CompoundTag> blockEntities = new ArrayList<>();
//            for (int i = 0; i < pc.getTileEntities().length; i++) {
//                blockEntities.add(ItemBlockTranslator.translateBlockEntityToPE(pc.getTileEntities()[i]));
//            }
//            pe.blockEntities = NBTIO.write(blockEntities, ByteOrder.LITTLE_ENDIAN, true);
//        } catch (IOException ex) {
//            Logger.getLogger(PCChunkDataPacketTranslator.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        // I tried that horrible way too ><
//        for (int i = 0; i < pc.getTileEntities().length; i++) {
//            BlockEntityDataPacket data = new BlockEntityDataPacket();
//            data.tag = ItemBlockTranslator.translateBlockEntityToPE(pc.getTileEntities()[i]);
//            data.blockPosition = new BlockPosition(data.tag.getInt("x"), data.tag.getInt("y"), data.tag.getInt("z"));
//            this.session.putCachePacket(data);
//        }
    }

    private static int index(int x, int y, int z) {
        return (x << 8) | (z << 4) | (y & 0xF);
    }

    private static int dataIndex(int x, int y, int z) {
        return (x << 7) | (z << 3) | ((y & 0xF) >> 1);
    }
}
