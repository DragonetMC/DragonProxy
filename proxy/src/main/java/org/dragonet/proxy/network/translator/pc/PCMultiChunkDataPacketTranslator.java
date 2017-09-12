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
import java.util.Arrays;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.apache.commons.lang3.ArrayUtils;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.pocket113.play.FullChunkData;
import sul.protocol.pocket113.types.ChunkData;
import sul.protocol.pocket113.types.Section;
import sul.utils.Packet;
import sul.utils.Tuples;

public class PCMultiChunkDataPacketTranslator implements PCPacketTranslator<ServerChunkDataPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerChunkDataPacket packet) {

        session.getProxy().getGeneralThreadPool().execute(() -> {
    		try {

				FullChunkData pePacket = new FullChunkData();
				pePacket.position = new Tuples.IntXZ(packet.getColumn().getX(), packet.getColumn().getZ());

				ChunkData peChunk = new ChunkData();
				peChunk.sections = new Section[packet.getColumn().getChunks().length];
				for(int cy = 0; cy < packet.getColumn().getChunks().length; cy++) {
					Chunk pcChunk = packet.getColumn().getChunks()[cy];
					peChunk.sections[cy] = new Section();
					if(pcChunk != null) {
						processChunkSection(pcChunk, peChunk.sections[cy]);
					}
				}
				pePacket.data = peChunk;
				session.sendPacket(pePacket, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        );


        return null;
    }

    private void processChunkSection(Chunk pc, Section pe) {
		Arrays.fill(pe.blockIds, (byte)1);
		/*
		for(int y = 0; y < 16; y++) {
			for(int z = 0; z < 16; z++) {
				for(int x = 0; x < 16; x++) {
					BlockState block = pc.getBlocks().get(x, y, z);
					pe.blockIds[index(x, y, z)] = (byte) (ItemBlockTranslator.translateToPE(block.getId()) & 0xFF);
					if(x % 2 == 0) {
						pe.blockMetas[index(x,y,z)/2] = (byte) ((block.getData() << 8) | (pc.getBlocks().get(x+1, y,z).getData() & 0xF));
						pe.skyLight[index(x,y,z)/2] = (byte) 0xFF;
						pe.blockLight[index(x,y,z)/2] = (byte) 0xFF;
					}
				}
			}
		}
		*/
	}

	private static int index(int x, int y, int z) {
		return y << 8 | z << 4 | x;
	}
}
