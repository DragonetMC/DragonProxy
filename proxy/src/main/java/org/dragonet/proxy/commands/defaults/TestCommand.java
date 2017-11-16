package org.dragonet.proxy.commands.defaults;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.protocol.packets.FullChunkDataPacket;
import org.dragonet.proxy.protocol.packets.MovePlayerPacket;
import org.dragonet.proxy.protocol.packets.PlayStatusPacket;
import org.dragonet.proxy.protocol.packets.ResourcePacksInfoPacket;
import org.dragonet.proxy.protocol.type.chunk.ChunkData;
import org.dragonet.proxy.protocol.type.chunk.Section;
import org.dragonet.proxy.utilities.Vector3F;

import java.util.Arrays;

/**
 * Created on 2017/9/13.
 */
public class TestCommand extends Command {
	// vars

	// constructor
	public TestCommand(String name) {
		super(name);
	}

	// public
	public void execute(DragonProxy proxy, String[] args) {
		UpstreamSession player = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[1])[0];
		if (args[0].equalsIgnoreCase("status")) {
			PlayStatusPacket s = new PlayStatusPacket();
			s.status = PlayStatusPacket.PLAYER_SPAWN;
			player.sendPacket(s);
		} else if (args[0].equalsIgnoreCase("res")) {
			player.sendPacket(new ResourcePacksInfoPacket());
		} else if (args[0].equalsIgnoreCase("spawnpos")) {
			player.sendChat("spawn at: " + player.getEntityCache().getClientEntity().x + ", "
					+ player.getEntityCache().getClientEntity().y + ", " + player.getEntityCache().getClientEntity().z);
		} else if (args[0].equalsIgnoreCase("tp")) {
			Vector3F dest = new Vector3F(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
					Float.parseFloat(args[3]));
			MovePlayerPacket m = new MovePlayerPacket();
			m.mode = MovePlayerPacket.MODE_TELEPORT;
			m.position = dest;
			player.sendPacket(m);
			player.sendChat("\u00a7bTeleported to: " + dest.toString());
		} else if (args[0].equalsIgnoreCase("chunk")) {
			/*
			 * FullChunkData chunk = new FullChunkData(Integer.parseInt(args[1]),
			 * Integer.parseInt(args[2])); Arrays.fill(chunk.ids, (byte)1);
			 */
			ChunkData data = new ChunkData();
			data.sections = new Section[16];
			for (int cy = 0; cy < 16; cy++) {
				data.sections[cy] = new Section();
				Arrays.fill(data.sections[cy].blockIds, (byte) 1);
			}
			FullChunkDataPacket chunk = new FullChunkDataPacket();
			chunk.x = Integer.parseInt(args[1]);
			chunk.z = Integer.parseInt(args[2]);
			data.encode();
			chunk.payload = data.getBuffer();
			player.sendPacket(chunk);
		}
	}

	// private

}
