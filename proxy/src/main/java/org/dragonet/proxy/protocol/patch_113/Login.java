package org.dragonet.proxy.protocol.patch_113;

import org.dragonet.proxy.utilities.BinaryStream;
import sul.utils.Packet;

/**
 * Created on 2017/9/12.
 */
public class Login extends Packet {

    public static final byte ID = (byte)1;

    public static final boolean CLIENTBOUND = false;
    public static final boolean SERVERBOUND = true;

    @Override
    public int getId() {
        return ID;
    }

    // version
    public static final byte VANILLA = (byte)0;
    public static final byte EDUCATION = (byte)1;

    /**
     * Version of the protocol used by the player.
     */
    public int protocol = 113;

    /**
     * Edition that the player is using to join the server. The different editions may
     * have different features and servers may block the access from unaccepted editions
     * of the game.
     */
    public byte version;

    public byte[] chain_data;
    public byte[] client_data;

    public Login() {}

    public Login(int protocol, byte version) {
        this.protocol = protocol;
        this.version = version;
    }

    @Override
    public int length() {
        return 0; // for now only decoding
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] buffer) {
        this._buffer = buffer;
        readBigEndianByte();
        protocol=readBigEndianInt();
        version=readBigEndianByte();
        readBigEndianByte(); // skip this shiet, idk who put it there

        int len_body = readVaruint();
        byte[] body_data = readBytes(len_body);

        BinaryStream body = new BinaryStream(body_data);
        chain_data = body.get(body.getLInt());
        client_data = body.get(body.getLInt());
    }

    public static Login fromBuffer(byte[] buffer) {
        Login ret = new Login();
        ret.decode(buffer);
        return ret;
    }

    @Override
    public String toString() {
        return "Login(protocol: " + this.protocol + ", version: " + this.version + ")";
    }
}
