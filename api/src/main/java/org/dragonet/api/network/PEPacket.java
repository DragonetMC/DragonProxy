/*
 * DragonProxy API
 * Copyright © 2016 Dragonet Foundation (https://github.com/DragonetMC/DragonProxy)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dragonet.api.network;

import org.dragonet.common.utilities.BinaryStream;

/**
 * Created on 2017/10/21.
 */
public abstract class PEPacket extends BinaryStream {

    private boolean encoded;
    private boolean decoded;

    public PEPacket() {
        super();
    }

    public boolean isEncoded() {
        return encoded;
    }

    public boolean isDecoded() {
        return decoded;
    }

    public void encode() {
        reset();
        encodeHeader();
        encodePayload();
        encoded = true;
    }

    public void decode() {
        decodeHeader();
        decodePayload();
        decoded = true;
    }

    public void encodeHeader() {
        // putUnsignedVarInt(pid());
        putByte((byte) (pid() & 0xFF));
        putByte((byte) 0x00);
        putByte((byte) 0x00);
    }

    public void decodeHeader() {
        getByte(); // getUnsignedVarInt();
        get(2);
    }

    public abstract int pid();

    public abstract void encodePayload();

    public abstract void decodePayload();

}
