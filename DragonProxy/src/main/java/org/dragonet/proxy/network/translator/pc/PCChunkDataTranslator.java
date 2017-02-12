package org.dragonet.proxy.network.translator.pc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.chunk.Chunk;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import net.marfgamer.jraknet.RakNetPacket;
import sul.protocol.pocket101.play.FullChunkData;
import sul.utils.Tuples;

public class PCChunkDataTranslator implements PCPacketTranslator<ServerChunkDataPacket> {
	
	@Override
	public RakNetPacket[] translate(ClientConnection session, ServerChunkDataPacket packet) {
		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		DataOutputStream dos1 = new DataOutputStream(bos1);

		//ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		//DataOutputStream dos2 = new DataOutputStream(bos2);

		//ByteArrayOutputStream bosTiles = new ByteArrayOutputStream();
		//DataOutputStream dosTiles = new DataOutputStream(bosTiles);

		
		
		try {
			bos1.reset();
			//bos2.reset();
			//bosTiles.reset();

			FullChunkData pePacket = new FullChunkData();
			
			pePacket.position = new Tuples.IntXZ(packet.getColumn().getX(), packet.getColumn().getZ());
			//pePacket.order = FullChunkDataPacket.ChunkOrder.COLUMNS;
			
			Chunk[] pcChunks = packet.getColumn().getChunks();
			
			
			for (int x = 0; x < 16; x++) { // Write Block ID's
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 112; y++) {
						if (pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty()) {
							dos1.writeByte((byte) 1);
						} else {
							//dos1.writeByte(1);
							
							int pcBlock = pcChunks[y >> 4].getBlocks().get(x, y % 16, z).getId();
							int peBlock = ItemBlockTranslator.translateToPE(pcBlock);
							//dos1.writeByte((byte) peBlock);
							dos1.writeByte((byte) (peBlock & 0xFF));
							// dos1.writeByte((byte) 1);
							
						}
					}
				}
			}
			
			for (int x = 0; x < 16; x++) {// Write Block Data
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 112; y += 2) {
						if (pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty()) {
							dos1.writeByte((byte) 0);
						} else {
							byte data1 = 0;
							byte data2 = 0;
							try {
								data1 = (byte) pcChunks[y >> 4].getBlocks().get(x, y % 16, z).getData();
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								data2 = (byte) pcChunks[y >> 4].getBlocks().get(x, (y + 1) % 16, z).getData();
							} catch (Exception e) {
								e.printStackTrace();
							}

							data1 |= ((data2 & 0xF) << 4);

							dos1.writeByte(data1);
						}
					}
				}
			}
			
			for (int x = 0; x < 16; x++) { // Write Skylight
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 112; y += 2) {
						if (pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty()) {
							dos1.writeByte((byte)15);
						} else {
							//if (noSkyLight) {
							//	temp.writeByte(0);
							//} else {
							byte data = 0;
							try {
								data = (byte) (pcChunks[y >> 4].getSkyLight().get(x, y & 0xF, z) & 0xF);
								data |= (pcChunks[y >> 4].getSkyLight().get(x, (y + 1) & 0xF, z) & 0xF);
							} catch (Exception e) {
								e.printStackTrace();
							}
							dos1.writeByte(data);
							//}
						}
					}
				}
			}
			
			//dos1.write(bos2.toByteArray()); //Not bos1 contains previously generated data! Don't reset! 
			//bos2.reset();//Now it's empty
			
			for (int x = 0; x < 16; x++) { // Write Block Light
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 112; y += 2) {
						byte data = pcChunks[y >> 4] == null || pcChunks[y >> 4].isEmpty() ? (byte) 15 : (byte) ((pcChunks[y >> 4].getBlockLight().get(x, y % 16, z) & 0xF) << 4);
						data |= pcChunks[(y + 1) >> 4] == null || pcChunks[(y + 1) >> 4].isEmpty() ? (byte) 15 : (byte) (pcChunks[(y + 1) >> 4].getBlockLight().get(x, (y + 1) % 16, z) & 0xF);
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
			
			//bos2.reset();

			dos1.writeInt(0);//Extra data, should be little-endian but it's 0 here for now so it's okay. 

			//dos1.write(bosTiles.toByteArray());
			
			pePacket.tiles = new byte[0];
            pePacket.data = bos1.toByteArray();
            return fromSulPackets(pePacket);
        } catch (Exception e) {
        	System.err.println("Error while processing ChunkData packet");
        	e.printStackTrace();
        }
		return new RakNetPacket[0];
    }

	/*@Override
	public DataPacket[] translate(ClientConnection session, ServerChunkDataPacket packet) {
		FullChunkDataPacket pePacket = new FullChunkDataPacket();
		
		try {
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			DataOutputStream dos1 = new DataOutputStream(bos1);
			
			cn.nukkit.level.format.mcregion.Chunk chunk = cn.nukkit.level.format.mcregion.Chunk.getEmptyChunk(0, 0);
			
			pePacket.chunkX = packet.getColumn().getX();
			pePacket.chunkZ = packet.getColumn().getZ();
			
			Chunk[] pcChunks = packet.getColumn().getChunks();
			
			byte[] biomes = packet.getColumn().getBiomeData();
			
			int yPE = 0;
			for(int index = 0; index < pcChunks.length; index++){
				for(int yPC = 0; yPC < 16; yPC++, yPE++){
					for(int x = 0; x < 16; x++){
						for(int z = 0; z < 16; z++){
							int blockID = 0; // Air ID
							int blockData = 0; // No Data
							int blockLight = 15; // Max Light
							int skyLight = 15; // Max Light
							if(pcChunks[index] != null && !pcChunks[index].isEmpty()){
								try {
									blockID = pcChunks[index].getBlocks().get(x, yPC, z).getId();
									blockData = pcChunks[index].getBlocks().get(x, yPC, z).getData();
									blockLight = pcChunks[index].getBlockLight().get(x, yPC, z);
									skyLight = pcChunks[index].getSkyLight().get(x, yPC, z);
								} catch (Exception e){
									System.err.println("X: " + x + ", Z: " + z + ", yPC: " + yPC + ", yPE: " + yPE + ", Index: " + index + ", pcChunks.length: " + pcChunks.length);
									e.printStackTrace();
								}
							}
							chunk.setBlockId(x, yPE, z, blockID);
							chunk.setBlockData(x, yPE, z, blockData);
							chunk.setBlockLight(x, yPE, z, blockLight);
							chunk.setBlockSkyLight(x, yPE, z, skyLight);
							chunk.setBiomeId(x, z, biomes[z <<  4 | x]);
						}
					}
				}
			}
			// ID's
			dos1.write(chunk.getBlockIdArray());
			
			// Data
			dos1.write(chunk.getBlockDataArray());
			
			// Light
			dos1.write(chunk.getBlockLightArray());
			
			// Skylight
			dos1.write(chunk.getBlockSkyLightArray());
			
			//Height Map
			for (int i = 0; i < 256; i++) {
				dos1.writeByte((byte) 0xFF);
			}
			//dos1.write(chunk.getHeightMapArray());

			//Biome Colors
/*			for (int i = 0; i < 256; i++) {
				dos1.writeByte((byte) 0x01);
				dos1.writeByte((byte) 0x85);
				dos1.writeByte((byte) 0xB2);
				dos1.writeByte((byte) 0x4A);
			}
			dos1.write(chunk.getBiomeIdArray());
			
			//bos2.reset();

			dos1.writeInt(0);//Extra data, should be little-endian but it's 0 here for now so it's okay. 

			//dos1.write(bosTiles.toByteArray());
			
	        pePacket.data = bos1.toByteArray();
	        //session.sendPacket(pePacket, true);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return new DataPacket[] { pePacket };
	}*/
}

