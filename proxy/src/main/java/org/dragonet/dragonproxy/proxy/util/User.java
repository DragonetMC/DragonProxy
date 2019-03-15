package org.dragonet.dragonproxy.proxy.util;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.Client;
import com.nukkitx.network.raknet.RakNetClient;
import com.nukkitx.network.raknet.session.RakNetSession;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.PlayerSession;
import com.nukkitx.protocol.bedrock.session.BedrockSession;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Objects;

public final class User implements PlayerSession, AutoCloseable {
    private static final LinkedHashMap<InetSocketAddress, User> users = new LinkedHashMap<>();

    private Client steveice10Client;
    private RakNetClient<BedrockSession<User>> client;
    private Server server;
    private InetSocketAddress remoteAddress;
    private InetSocketAddress localAddress;
    private UserType type;
    private RakNetSession s;
    private Position pos;
    private long id;

    private boolean closed;

    private User(InetSocketAddress remote, InetSocketAddress local, Client client, UserType type) {
        this.closed = false;
        this.steveice10Client = client;
        this.type = type;
        this.localAddress = local;
        users.put(remote, this);
    }

    @Override
    public void onDisconnect(@Nonnull String s) {

    }

    @Override
    public void onDisconnect(@Nonnull DisconnectReason disconnectReason) {

    }

    @Override
    public void close() {
        remove(this.remoteAddress);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server s) {
        this.server = s;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public static User add(Client c, InetSocketAddress remote, InetSocketAddress local, UserType type){
        if(users.containsKey(remote)) {
            return null;
        }
        return new User(Objects.requireNonNull(remote), Objects.requireNonNull(local), Objects.requireNonNull(c), Objects.requireNonNull(type));
    }

    public static User get(InetSocketAddress a) {
        return users.get(a);
    }

    public static void remove(InetSocketAddress a) {
        User user = users.get(a);

        //If someone still has a reference to this, null everything else for garbage collector
        user.closed = true;
        user.server = null;
        user.s = null;
        user.remoteAddress = null;
        user.localAddress = null;
        user.client = null;
        user.steveice10Client = null;
        user.type = null;

        //unregister
        users.remove(a);
    }

    public enum UserType {
        //What kind of user is this?
        BEDROCK,
        JAVA
    }
}
