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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import sul.protocol.pocket105.play.Batch;
import sul.protocol.pocket113.Packets;
import sul.utils.Packet;

public final class Protocol {

    public static Packet decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        int pid = data[0] & 0xFF;
        if (Packets.PLAY.containsKey(pid)) {
            Class<? extends Packet> c = Packets.PLAY.get(pid);
            // remove the padding
            byte[] decodable = ArrayUtils.remove(data, 1);
            decodable = ArrayUtils.remove(decodable, 1);
            try {
                Packet pk = c.newInstance();
                pk.decode(decodable);
                return pk;
            } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException ex) {
            }
        }
        return null;
    }
}
