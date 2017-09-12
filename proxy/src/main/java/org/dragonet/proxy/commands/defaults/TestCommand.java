package org.dragonet.proxy.commands.defaults;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.network.UpstreamSession;
import sul.protocol.pocket113.play.FullChunkData;
import sul.protocol.pocket113.play.MovePlayer;
import sul.protocol.pocket113.play.PlayStatus;
import sul.protocol.pocket113.play.ResourcePacksInfo;
import sul.protocol.pocket113.types.ChunkData;
import sul.protocol.pocket113.types.Section;
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
            ChunkData data = new ChunkData();
            data.sections = new Section[16];
            for(int i = 0; i < 16; i++) {
                data.sections[i] = new Section();
                Arrays.fill(data.sections[i].blockIds, (byte)1);
            }
            FullChunkData chunk = new FullChunkData(new Tuples.IntXZ(0, 0), data);
            player.sendPacket(chunk);
        }
    }
}
