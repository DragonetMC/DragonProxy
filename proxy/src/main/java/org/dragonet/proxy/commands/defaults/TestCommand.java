package org.dragonet.proxy.commands.defaults;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.network.UpstreamSession;
import sul.protocol.bedrock137.play.MovePlayer;
import sul.protocol.bedrock137.play.PlayStatus;
import sul.protocol.bedrock137.play.ResourcePacksInfo;
import sul.protocol.bedrock137.types.ChunkData;
import sul.protocol.bedrock137.types.Section;
import sul.utils.Tuples;

import java.util.Arrays;

/**
 * Created on 2017/9/13.
 */
public class TestCommand extends Command {
    public TestCommand(String name) {
        super(name);
    }

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession player = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[1])[0];
        if(args[0].equalsIgnoreCase("status")) {
            PlayStatus s = new PlayStatus(3);
            player.sendPacket(s);
        } else if(args[0].equalsIgnoreCase("res")) {
            player.sendPacket(new ResourcePacksInfo());
        } else if(args[0].equalsIgnoreCase("spawnpos")) {
            player.sendChat("spawn at: " + player.getEntityCache().getClientEntity().getX() + ", " + player.getEntityCache().getClientEntity().getY() + ", " + player.getEntityCache().getClientEntity().getZ());
        } else if(args[0].equalsIgnoreCase("tp")) {
            Tuples.FloatXYZ dest = new Tuples.FloatXYZ(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
            MovePlayer m = new MovePlayer(0L,
                    dest,
                    0f, 0f, 0f, MovePlayer.TELEPORT, true, 0L, 0, 0);
            player.sendPacket(m);
            player.sendChat("\u00a7bTeleported to: " + dest.toString());
        } else if(args[0].equalsIgnoreCase("chunk")) {
            /*
            FullChunkData chunk = new FullChunkData(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            Arrays.fill(chunk.ids, (byte)1);
            */
            ChunkData data = new ChunkData();
            data.sections = new Section[16];
            for(int cy = 0; cy < 16; cy++) {
                data.sections[cy] = new Section();
                Arrays.fill(data.sections[cy].blockIds, (byte)1);
            }
            sul.protocol.bedrock137.play.FullChunkData chunk = new sul.protocol.bedrock137.play.FullChunkData(new Tuples.IntXZ(Integer.parseInt(args[1]), Integer.parseInt(args[2])), data);
            player.sendPacket(chunk);
        }
    }
}
