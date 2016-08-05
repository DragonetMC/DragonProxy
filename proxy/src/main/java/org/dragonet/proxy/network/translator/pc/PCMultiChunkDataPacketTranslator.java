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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.dragonet.proxy.protocol.packet.FullChunkPacket;
import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiChunkDataPacket;

public class PCMultiChunkDataPacketTranslator implements PCPacketTranslator<ServerMultiChunkDataPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerMultiChunkDataPacket packet) {

        session.getProxy().getGeneralThreadPool().execute(() -> {
    		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
    		DataOutputStream dos1 = new DataOutputStream(bos1);

    		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
    		DataOutputStream dos2 = new DataOutputStream(bos2);

    		ByteArrayOutputStream bosTiles = new ByteArrayOutputStream();
    		DataOutputStream dosTiles = new DataOutputStream(bosTiles);

    		try {
    			for (int col = 0; col < packet.getColumns(); col++) {
    				bos1.reset();
    				bos2.reset();
    				bosTiles.reset();

    				FullChunkPacket pePacket = new FullChunkPacket();

    				pePacket.chunkX = packet.getX(col);
    				pePacket.chunkZ = packet.getZ(col);
    				pePacket.order = FullChunkPacket.ChunkOrder.COLUMNS;
    				Chunk[] pcChunks = packet.getChunks(col);
    				for (int x = 0; x < 16; x++) {
    					for (int z = 0; z < 16; z++) {
    						for (int y = 0; y < 128; y++) {
    							if (pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty()) {
    								dos1.writeByte((byte) 0);
    							} else {
    								int pcBlock = pcChunks[y >> 4].getBlocks().getBlock(x, y % 16, z);
    								int peBlock = pcBlock;
    								peBlock = ItemBlockTranslator.translateToPE(peBlock);
    								dos1.writeByte((byte) (peBlock & 0xFF));
    								// dos1.writeByte((byte) 1);
    							}
    						}
    					}
    				}
    				for (int x = 0; x < 16; x++) {
    					for (int z = 0; z < 16; z++) {
    						for (int y = 0; y < 128; y += 2) {
    							byte data1 = 0;
    							byte data2 = 0;
    							try {
    								data1 = (byte) pcChunks[y >> 4].getBlocks().getData(x, y % 16, z);
    							} catch (Exception e) {

    							}
    							try {
    								data2 = (byte) pcChunks[y >> 4].getBlocks().getData(x, (y + 1) % 16, z);
    							} catch (Exception e) {

    							}

    							data1 |= ((data2 & 0xF) << 4);

    							dos1.writeByte(data1);
    						}
    					}
    				}
    				for (int x = 0; x < 16; x++) {
    					for (int z = 0; z < 16; z++) {
    						for (int y = 0; y < 128; y += 2) {
    							//if (noSkyLight) {
    							//	temp.writeByte(0);
    							//} else {
    							byte data = 0;
    							try {
    								data = (byte) (pcChunks[y >> 4].getSkyLight().get(x, y & 0xF, z) & 0xF);
    								data |= (pcChunks[y >> 4].getSkyLight().get(x, (y + 1) & 0xF, z) & 0xF);
    							} catch (Exception e) {

    							}
    							dos1.writeByte(data);
    							//}
    						}
    					}
    				}
    				dos1.write(bos2.toByteArray()); //Not bos1 contains previously generated data! Don't reset! 
    				bos2.reset();//Now it's empty
    				for (int x = 0; x < 16; x++) {
    					for (int z = 0; z < 16; z++) {
    						for (int y = 0; y < 128; y += 2) {
    							byte data = pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty() ? (byte) 0 : (byte) ((pcChunks[y >> 4].getBlockLight().get(x, y % 16, z) & 0xF) << 4);
    							data |= pcChunks[(y + 1) >> 4] == null || pcChunks[(y + 1) >> 4].isEmpty() ? (byte) 0 : (byte) (pcChunks[(y + 1) >> 4].getBlockLight().get(x, (y + 1) % 16, z) & 0xF);
    							dos1.writeByte(data);
    						}
    					}
    				}
    				//Height Map
    				for (int i = 0; i < 256; i++) {
    					dos1.writeByte((byte) 0xFF);
    				}

    				//Biome Colors
    				for (int i = 0; i < 256; i++) {
    					dos1.writeByte((byte) 0x01);
    					dos1.writeByte((byte) 0x85);
    					dos1.writeByte((byte) 0xB2);
    					dos1.writeByte((byte) 0x4A);
    				}

    				bos2.reset();

    				dos1.writeInt(0);//Extra data, should be little-endian but it's 0 here for now so it's okay. 

    				dos1.write(bosTiles.toByteArray());

                    pePacket.chunkData = bos1.toByteArray();
                    session.sendPacket(pePacket, true);
                }
            } catch (Exception e) {
            }
        }
        );

        return null;
    }
}
