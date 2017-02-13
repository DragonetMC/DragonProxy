package org.dragonet.proxy.network.translator.pc;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dragonet.proxy.network.ClientConnection;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.chunk.Chunk;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

import net.marfgamer.jraknet.RakNetPacket;
import org.dragonet.proxy.network.PacketTranslatorRegister;
import org.spacehq.mc.protocol.data.game.chunk.BlockStorage;
import org.spacehq.mc.protocol.data.game.chunk.Column;
import org.spacehq.mc.protocol.data.game.world.block.BlockState;
import sul.protocol.pocket101.play.FullChunkData;
import sul.utils.Tuples;

public class PCChunkDataTranslator implements PCPacketTranslator<ServerChunkDataPacket> {

    @Override
    public RakNetPacket[] translate(ClientConnection session, ServerChunkDataPacket packet) {
        Column col = packet.getColumn();
        cn.nukkit.level.format.anvil.Chunk chunk = cn.nukkit.level.format.anvil.Chunk.getEmptyChunk(col.getX(), col.getZ());

        // Fill the empty chunk with the packet's data
        for (Chunk chk : col.getChunks()) {
            if (chk != null && !chk.isEmpty()) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 16; y++) {
                            BlockState blk = chk.getBlocks().get(x, y, z);
                            chunk.setBlock(x, y, z, blk.getId(), blk.getData());
                            chunk.setBlockLight(x, y, z, chk.getBlockLight().get(x, y, z));
                            chunk.setBlockSkyLight(x, y, z, chk.getSkyLight().get(x, y, z));
                        }
                    }
                }
            }
        }

        //chunk.getBlockEntities();
        //col.getTileEntities();
        // Use the code from cn.nukkit.level.format.anvil.Anvil.requestChunkTask(); to encode the chunk data
        byte[] blockEntities = new byte[0];
        if (!chunk.getBlockEntities().isEmpty()) {
            List<CompoundTag> tagList = new ArrayList<>();

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof BlockEntitySpawnable) {
                    tagList.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound());
                }
            }

            try {
                blockEntities = NBTIO.write(tagList, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Integer, Integer> extra = chunk.getBlockExtraDataArray();
        BinaryStream extraData;
        if (!extra.isEmpty()) {
            extraData = new BinaryStream();
            extraData.putVarInt(extra.size());
            for (Map.Entry<Integer, Integer> entry : extra.entrySet()) {
                extraData.putVarInt(entry.getKey());
                extraData.putLShort(entry.getValue());
            }
        } else {
            extraData = null;
        }

        BinaryStream stream = new BinaryStream();
        int count = 0;
        cn.nukkit.level.format.ChunkSection[] sections = chunk.getSections();
        for (int i = sections.length - 1; i >= 0; i--) {
            if (!sections[i].isEmpty()) {
                count = i + 1;
                break;
            }
        }
        stream.putByte((byte) count);
        for (int i = 0; i < count; i++) {
            stream.putByte((byte) 0);
            stream.put(sections[i].getBytes());
        }
        for (int height : chunk.getHeightMapArray()) {
            stream.putByte((byte) height);
        }
        stream.put(new byte[256]);
        stream.put(col.getBiomeData());
        stream.putByte((byte) 0);
        if (extraData != null) {
            stream.put(extraData.getBuffer());
        } else {
            stream.putVarInt(0);
        }

        //stream.put(blockEntities);

        /*int y = 1;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk.setBlock(x, y, z, 7);
            }
        }*/
        int length = 0;
        length += 1; // Section count
        length += (chunk.getSections().length * (/*Section Size*/10240)) + (chunk.getSections().length /*Section header*/); // blocks[4096] + data[2048] + skyLight[2048] + blockLight[2048] 
        length += 256; // Height Map
        length += 256; // Unknown
        length += (4 * 256); // Biome ID's
        length += 4; // Varint for extradata replace with extradata len if sending extradata
        length += 1; // Block entities; same as varint above

        FullChunkData pePacket = new FullChunkData(new Tuples.IntXZ(chunk.getX(), chunk.getZ()), stream.getBuffer(), blockEntities);
        /*int index = 0;
        pePacket.data[index++] = (byte) (chunk.getSections().length);
        for (int ind = chunk.getSections().length - 1; ind >= 0; ind--) {
            cn.nukkit.level.format.ChunkSection sec = chunk.getSection(ind);
            pePacket.data[index++] = 0; // Version Header?
            for (byte b : sec.getBytes()) {
                pePacket.data[index++] = b;
            }
        }*/
        return PacketTranslatorRegister.preparePacketsForSending(pePacket);
    }
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
