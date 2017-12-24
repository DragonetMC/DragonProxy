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
package org.dragonet.proxy.utilities.io;

import java.util.Arrays;

public final class ArraySplitter {

    public ArraySplitter() {

    }

    public static byte[][] splitArray(byte[] array, int singleSlice) {
        if (array.length <= singleSlice) {
            byte[][] singleRet = new byte[1][];
            singleRet[0] = array;
            return singleRet;
        }
        byte[][] ret = new byte[(array.length / singleSlice + (array.length % singleSlice == 0 ? 0 : 1))][];
        int pos = 0;
        int slice = 0;
        while (slice < ret.length) {
            if (pos + singleSlice < array.length) {
                ret[slice] = Arrays.copyOfRange(array, pos, pos + singleSlice);
                pos += singleSlice;
                slice++;
            } else {
                ret[slice] = Arrays.copyOfRange(array, pos, array.length);
                pos += array.length - pos;
                slice++;
            }
        }
        return ret;
    }
}
