package org.dragonet.proxy.commands.defaults;

import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.packets.*;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.common.gui.CustomFormComponent;
import org.dragonet.common.gui.DropDownComponent;
import org.dragonet.common.gui.LabelComponent;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.protocol.type.chunk.ChunkData;
import org.dragonet.protocol.type.chunk.Section;
import org.dragonet.common.maths.BlockPosition;


import java.util.Arrays;

/**
 * Created on 2017/9/13.
 */
public class TestCommand extends Command {

    public TestCommand(String name) {
        super(name);
    }

    public void execute(DragonProxy proxy, String[] args) {
        if (args.length == 0) {
            System.out.println("This is a developer's command! ");
            return;
        }
        UpstreamSession player = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[1])[0];
        if (args[0].equalsIgnoreCase("status")) {
            PlayStatusPacket s = new PlayStatusPacket();
            s.status = PlayStatusPacket.PLAYER_SPAWN;
            player.sendPacket(s);
        } else if (args[0].equalsIgnoreCase("res")) {
            player.sendPacket(new ResourcePacksInfoPacket());
        } else if (args[0].equalsIgnoreCase("pos")) {
            player.sendChat("pos at: " + player.getEntityCache().getClientEntity().x + ", "
                + player.getEntityCache().getClientEntity().y + ", " + player.getEntityCache().getClientEntity().z);
        } else if (args[0].equalsIgnoreCase("respawn")) {
            RespawnPacket resp = new RespawnPacket();
            resp.position = new Vector3F(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
                Float.parseFloat(args[3]));
            player.sendPacket(resp);
        } else if (args[0].equalsIgnoreCase("chunkradius")) {
            player.sendPacket(new ChunkRadiusUpdatedPacket(8));
        } else if (args[0].equalsIgnoreCase("setspawnpos")) {
            SetSpawnPositionPacket packetSetSpawnPosition = new SetSpawnPositionPacket();
            packetSetSpawnPosition.position = new BlockPosition(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            player.sendPacket(packetSetSpawnPosition);
        } else if (args[0].equalsIgnoreCase("motion")) {
            SetEntityMotionPacket mot = new SetEntityMotionPacket();
            mot.rtid = 1L;
            mot.motion = new Vector3F(0f, 0f, 0f);
            player.sendPacket(mot);
        } else if (args[0].equalsIgnoreCase("die")) {
            player.sendPacket(new SetHealthPacket(0));
        } else if (args[0].equalsIgnoreCase("tp")) {
            Vector3F dest = new Vector3F(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
                Float.parseFloat(args[3]));
            MovePlayerPacket m = new MovePlayerPacket();
            m.rtid = 1L;
            m.mode = (byte) (Integer.parseInt(args[4]) & 0xFF);
            m.position = dest;
            player.sendPacket(m);
            player.sendChat("\u00a7bTeleported to: " + dest.toString());
        } else if (args[0].equalsIgnoreCase("moveentity")) {
            Vector3F dest = new Vector3F(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
                Float.parseFloat(args[3]));
            MoveEntityAbsolutePacket m = new MoveEntityAbsolutePacket();
            m.rtid = 1L;
            m.teleported = args[4].equalsIgnoreCase("true");
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
        } else if (args[0].equalsIgnoreCase("form")) {
            testForm(player);
        } else if (args[0].equalsIgnoreCase("sound")) {

            int id = Integer.parseInt(args[1]);

            LevelSoundEventPacket pk = new LevelSoundEventPacket();

            CachedEntity self = player.getEntityCache().getClientEntity();

            pk.position = new Vector3F((float) self.x, (float) self.y, (float) self.z);
            pk.sound = LevelSoundEventPacket.Sound.fromID(id);

            player.sendPacket(pk);
            player.sendChat("\u00a7bSound ID " + pk.sound.soundID + " (" + pk.sound.name() + ") sent");

        } else if (args[0].equalsIgnoreCase("painting")) {

            AddPaintingPacket pk = new AddPaintingPacket();
            pk.rtid = player.getEntityCache().getNextAtomicLong().incrementAndGet();
            pk.eid = pk.rtid;
            pk.pos = new BlockPosition(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            pk.direction = Integer.parseInt(args[4]);
            pk.title = args[5];
            
            player.sendPacket(pk);
            player.sendChat("\u00a7bPainting " + pk.title + " spawned at " + pk.pos.toString());

        }
    }

    public static void testForm(UpstreamSession player) {
        ModalFormRequestPacket p = new ModalFormRequestPacket();
        CustomFormComponent form = new CustomFormComponent("\u00a7dTest Form");
        form.addComponent(new LabelComponent("\u00a71Text \u00a7ki"));
        form.addComponent(new LabelComponent("LABEL 2"));
        form.addComponent(new DropDownComponent("DROP DOWN", Arrays.asList("option 1", "option 2")));
        System.out.println(form.serializeToJson().toString());
        p.formId = 1;
        p.formData = form.serializeToJson().toString();
        player.sendPacket(p);
    }
}
