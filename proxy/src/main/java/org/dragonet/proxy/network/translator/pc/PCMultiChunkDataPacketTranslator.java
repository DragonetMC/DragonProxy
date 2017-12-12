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
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator.ItemEntry;
import org.dragonet.proxy.protocol.PEPacket;
import org.dragonet.proxy.protocol.packets.FullChunkDataPacket;
import org.dragonet.proxy.protocol.type.chunk.ChunkData;
import org.dragonet.proxy.protocol.type.chunk.Section;

public class PCMultiChunkDataPacketTranslator implements IPCPacketTranslator<ServerChunkDataPacket> {
	// vars

	// constructor
	public PCMultiChunkDataPacketTranslator() {

	}

	// public
	public PEPacket[] translate(UpstreamSession session, ServerChunkDataPacket packet) {
		session.getProxy().getGeneralThreadPool().execute(() -> {
			try {

				FullChunkDataPacket pePacket = new FullChunkDataPacket();
				pePacket.x = packet.getColumn().getX();
				pePacket.z = packet.getColumn().getZ();

				ChunkData chunk = new ChunkData();
				processChunkSection(packet.getColumn().getChunks(), chunk);
				chunk.encode();
				pePacket.payload = chunk.getBuffer();

				session.putCachePacket(pePacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

	// private
	private void processChunkSection(Chunk[] pc, ChunkData pe) {
		/*
		 * pe.sections = new Section[16]; for(int i = 0; i < 16; i++) { pe.sections[i] =
		 * new Section(); if(i < 2) Arrays.fill(pe.sections[i].blockIds, (byte)1); }
		 */
		pe.sections = new Section[16];
		for (int i = 0; i < 16; i++) {
			pe.sections[i] = new Section();
		}
		for (int y = 0; y < 256; y++) {
			int cy = y >> 4;
			if (pc[cy] == null || pc[cy].isEmpty())
				continue;
			BlockStorage blocks = pc[cy].getBlocks();
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					BlockState block = blocks.get(x, y & 0xF, z);
					ItemEntry entry = ItemBlockTranslator.translateToPE(block.getId(), block.getData());

					Section section = pe.sections[cy];
					section.blockIds[index(x, y,
							z)] = entry.id.byteValue();

//					section.blockMetas[index(x, y,
//							z)] = entry.damage.byteValue();
				}
			}
		}
	}

	private static int index(int x, int y, int z) {
		return (x << 8) | (z << 4) | (y & 0xF);
	}
}
