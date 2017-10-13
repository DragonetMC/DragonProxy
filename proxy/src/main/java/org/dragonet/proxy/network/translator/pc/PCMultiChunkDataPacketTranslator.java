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
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import sul.protocol.bedrock137.play.FullChunkData;
import sul.protocol.bedrock137.types.ChunkData;
import sul.protocol.bedrock137.types.Section;
import sul.utils.Packet;
import sul.utils.Tuples;

public class PCMultiChunkDataPacketTranslator implements PCPacketTranslator<ServerChunkDataPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, ServerChunkDataPacket packet) {

        session.getProxy().getGeneralThreadPool().execute(() -> {
    		try {

				FullChunkData pePacket = new FullChunkData(new Tuples.IntXZ(packet.getColumn().getX(), packet.getColumn().getZ()), new ChunkData());

				processChunkSection(packet.getColumn().getChunks(), pePacket.data);

				session.sendPacket(pePacket, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        );


        return null;
    }

    private void processChunkSection(Chunk[] pc, ChunkData pe) {
		int maxY = pc.length << 4;
		if(maxY > 15) maxY = 15;
		pe.sections = new Section[16];
		for(int y = 0; y < maxY; y++) {
			int cy = y >> 4;
			if (pc[cy] == null || pc[cy].isEmpty()) continue;
			BlockStorage blocks = pc[cy].getBlocks();
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					BlockState block = blocks.get(x, y & 0xF, z);
					pe.sections[cy].blockIds[index(x, y, z)] = (byte) (ItemBlockTranslator.translateToPE(block.getId()) & 0xFF);
				}
			}
		}
	}

	private static int index(int x, int y, int z) {
		return (x << 8) | (z << 4) | (y & 0xF);
	}
}
