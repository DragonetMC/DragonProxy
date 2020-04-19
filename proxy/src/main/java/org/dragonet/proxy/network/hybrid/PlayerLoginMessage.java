package org.dragonet.proxy.network.hybrid;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dragonet.proxy.network.session.data.AuthData;
import org.dragonet.proxy.network.session.data.DeviceOS;
import org.dragonet.proxy.network.session.data.UIProfile;

import java.net.InetSocketAddress;

@Getter
@NoArgsConstructor
public class PlayerLoginMessage implements HybridMessage {
    private AuthData authData;
    private DeviceOS deviceOS;
    private UIProfile uiProfile;
    private String deviceModel;
    private String gameVersion;
    private String languageCode;
    private InetSocketAddress ipAddress;

    @Override
    public ByteArrayDataOutput encode(ByteArrayDataOutput out) {
        out.writeUTF(authData.getIdentity().toString());
        out.writeUTF(authData.getXuid());
        out.writeUTF(authData.getDisplayName());
        out.writeInt(deviceOS.ordinal());
        out.writeInt(uiProfile.ordinal());
        out.writeUTF(deviceModel);
        out.writeUTF(gameVersion);
        out.writeUTF(languageCode);
        out.writeBoolean(ipAddress != null);

        if(ipAddress != null) {
            out.writeUTF(ipAddress.getHostName());
            out.writeShort(ipAddress.getPort());
        }
        return out;
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        // one-way
    }

    @Override
    public String getId() {
        return "PlayerLogin";
    }
}
