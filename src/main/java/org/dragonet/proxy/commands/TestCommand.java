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
package org.dragonet.proxy.commands;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.dragonet.net.packet.minecraft.FullChunkPacket;
import org.dragonet.net.packet.minecraft.ChangeDimensionPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;

/*
 * Used for testing during development.
 * You do not need to use this command.
 */
public class TestCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession cli = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[0])[0];

        cli.sendChat("Initiating... ");

        /*if (args.length == 1 && args[0].equals("chunk_before")) {
            cli.sendChat("Sending far chunk... ");
            for(int x = 1490; x<1510; x++){
                for(int z = 1240; z<1260; z++){
                    sendFarChunk(cli, x, z);
                }
            }
            
            //try{
            //    Thread.sleep(2000L);
            //}catch(Exception e){}
        }

        cli.sendChat("Moving location... ");
        //MovePlayerPacket pkMove = new MovePlayerPacket(0, 40000.0f, 64.0f, 20000.0f, 0.0f, 0.0f, 0.0f, false);
        //pkMove.mode = MovePlayerPacket.MODE_RESET;
        //cli.sendPacket(pkMove, true);
        ChangeDimensionPacket pkDim = new ChangeDimensionPacket((byte)1, 40000, 72, 20000, (byte)0);
        cli.sendPacket(pkDim, true);
        
        if (args.length == 1 && args[0].equals("chunk_after")) {
            cli.sendChat("Sending far chunk... ");
            for(int x = 1490; x<1510; x++){
                for(int z = 1240; z<1260; z++){
                    sendFarChunk(cli, x, z);
                }
            }
        }*/
    }

    private void sendFarChunk(UpstreamSession cli, int x, int z) {
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
            FullChunkPacket pkChunk = new FullChunkPacket();
            pkChunk.chunkX = x;
            pkChunk.chunkZ = z;
            pkChunk.chunkData = bos.toByteArray();
            cli.sendPacket(pkChunk, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
