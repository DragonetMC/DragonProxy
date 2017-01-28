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
package org.dragonet.proxy.commands.defaults;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.network.ClientConnection;

/*
 * Used for testing during development.
 * You do not need to use this command.
 */
public class TestCommand extends Command {

    public TestCommand(String name) {
        super(name, "Testing");
    }

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        /*ClientConnection cli = proxy.getSessionRegister().getAll().values().toArray(new ClientConnection[0])[0];

        proxy.getLogger().info("Intializing...");
        sendFarChunk(cli, 150, 999);*/
    }

    private void sendFarChunk(ClientConnection cli, int x, int z) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] yPreset = new byte[128];
            Arrays.fill(yPreset, 0, 64, (byte) 1);
            Arrays.fill(yPreset, 64, 128, (byte) 0);
            for (int xz = 0; xz < 256; xz++) {
                bos.write(yPreset);
            }
            bos.write(new byte[16384]);
            byte[] lightPreset = new byte[16384];
            Arrays.fill(lightPreset, (byte) 0xFF);
            bos.write(lightPreset);
            bos.write(lightPreset);
            for (int i = 0; i < 256; i++) {
                bos.write((byte) 0xFF);
            }
            //Biome Colors
            for (int i = 0; i < 256; i++) {
                bos.write((byte) 0x01);
                bos.write((byte) 0x85);
                bos.write((byte) 0xB2);
                bos.write((byte) 0x4A);
            }
            bos.write(new byte[4]);
            /*FullChunkDataPacket pkChunk = new FullChunkDataPacket();
            pkChunk.chunkX = x;
            pkChunk.chunkZ = z;
            pkChunk.data = bos.toByteArray();
            //cli.sendPacket(pkChunk, true);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
