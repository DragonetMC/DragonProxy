package org.dragonet.proxy.hybrid.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dragonet.proxy.hybrid.HybridMessage;
import org.dragonet.proxy.hybrid.data.AuthData;
import org.dragonet.proxy.hybrid.data.DeviceOS;
import org.dragonet.proxy.hybrid.data.UIProfile;

import java.net.InetSocketAddress;
import java.util.UUID;

@Getter
@Setter
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
        out.writeBoolean(false); // encryption
        out.writeBoolean(ipAddress != null);

        if(ipAddress != null) {
            out.writeUTF(ipAddress.getHostName());
            out.writeShort(ipAddress.getPort());
        }
        return out;
    }

    @Override
    public void decode(ByteArrayDataInput in) {
        authData = new AuthData(in.readUTF(), UUID.fromString(in.readUTF()), in.readUTF());
        deviceOS = DeviceOS.values()[in.readInt()];
        uiProfile = UIProfile.values()[in.readInt()];
        deviceModel = in.readUTF();
        gameVersion = in.readUTF();
        languageCode = in.readUTF();
        in.readBoolean(); // encryption
        if(in.readBoolean()) { // ip forwarding enabled
            ipAddress = new InetSocketAddress(in.readUTF(), in.readShort());
        }
    }

    @Override
    public String getId() {
        return "PlayerLogin";
    }
}
