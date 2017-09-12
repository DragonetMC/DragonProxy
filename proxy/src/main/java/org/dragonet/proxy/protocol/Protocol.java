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
package org.dragonet.proxy.protocol;

import org.dragonet.proxy.protocol.patch_113.Login;
import org.dragonet.proxy.utilities.BinaryStream;
import org.dragonet.proxy.utilities.Zlib;
import sul.protocol.pocket113.Packets;
import sul.utils.Packet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Protocol {

    public final static Map<Integer, Class<? extends Packet>> PATCHES = new HashMap<>();

    static {
        PATCHES.put(1, Login.class);
    }

    public static Packet[] decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        int pid = data[0] & 0xFF;
        System.out.println("FIRST BYTE = " + Integer.toHexString(pid) + ", len = " + data.length);
        if(pid != 0xfe) return null;

        byte[] inflated;
        try {
            inflated = Zlib.inflate(Arrays.copyOfRange(data, 1, data.length));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ArrayList<Packet> packets = new ArrayList<>(2);
        BinaryStream stream = new BinaryStream(inflated);
        while(stream.offset < inflated.length) {
            byte[] buffer = stream.get(stream.getLShort());
            Packet decoded = decodeSingle(buffer);

            if(decoded != null) {
                packets.add(decoded);
            } else {
                System.out.println("decode fail");
            }
        }

        return packets.size() > 0 ? packets.toArray(new Packet[0]) : null;
    }

    private static Packet decodeSingle(byte[] buffer) {
        int pid = buffer[0] & 0xFF;
        if (PATCHES.containsKey(pid) || Packets.PLAY.containsKey(pid)) {
            Class<? extends Packet> c = PATCHES.containsKey(pid) ? PATCHES.get(pid) : Packets.PLAY.get(pid);
            try{
                FileOutputStream fos = new FileOutputStream("cap_" + c.getSimpleName() + ".bin");
                fos.write(buffer);
                fos.close();
            }catch(Exception e){}
            try {
                Packet pk = c.newInstance();
                pk.decode(buffer);
                return pk;
            } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("can not decode for pid 0x" + Integer.toHexString(pid));
        }
        return null;
    }
}
