package org.dragonet.protocol.packets;

import org.dragonet.api.network.PEPacket;
import org.dragonet.protocol.ProtocolInfo;
import org.dragonet.common.utilities.BinaryStream;
import org.dragonet.common.utilities.LoginChainDecoder;

/**
 * Created on 2017/10/21.
 */
public class LoginPacket extends PEPacket {

    public int protocol;
    public LoginChainDecoder decoded;

    public LoginPacket() {

    }

    @Override
    public int pid() {
        return ProtocolInfo.LOGIN_PACKET;
    }

    @Override
    public void decodePayload() {
        protocol = getInt();

        if (protocol != ProtocolInfo.CURRENT_PROTOCOL) {
            if (protocol > 0xffff) { // guess MCPE <= 1.1
                offset -= 6;
                protocol = getInt();
            }
            return; // Do not attempt to continue decoding for non-accepted protocols
        }

        byte[] payload = getByteArray();
        BinaryStream bin = new BinaryStream(payload);
        byte[] chain = bin.get(bin.getLInt());
        byte[] client = bin.get(bin.getLInt());
        decoded = new LoginChainDecoder(chain, client);
        decoded.decode();
    }

    @Override
    public void encodePayload() {
        // TODO
    }
}
