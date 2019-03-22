package org.dragonet.dragonproxy.proxy.util.users;

import com.nukkitx.network.SessionManager;
import com.nukkitx.network.raknet.session.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.session.BedrockSession;
import org.dragonet.dragonproxy.proxy.util.Server;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

public class BedrockUser extends User {
    public static final SessionManager<BedrockSession<BedrockUser>> MANAGER = new SessionManager<>();

    private BedrockUser(InetSocketAddress remote, InetSocketAddress local, RakNetSession s) {
        super(UserType.BEDROCK);
        if(!(MANAGER.get(remote) == null)) {
            throw new RuntimeException();
        }
        MANAGER.add(remote, new BedrockSession<>(s));
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectReason disconnectReason) {

    }

    @Override
    public void onDisconnect(@Nonnull String s) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }


    @Override
    public void setServer(Server s) {

    }

    @Override
    public Server getServer() {
        return null;
    }

    public static BedrockUser add(InetSocketAddress remote, InetSocketAddress local, RakNetSession session) {
        return new BedrockUser(remote, local, session);
    }

    public static BedrockUser get(InetSocketAddress s) {
        return MANAGER.get(s).getPlayer();
    }

}
