package org.dragonet.dragonproxy.proxy.util.users;

import com.nukkitx.protocol.PlayerSession;
import org.dragonet.dragonproxy.proxy.util.Server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class User implements PlayerSession, AutoCloseable {
    protected final UserType type;

    User(UserType type) {
        this.type = type;
    }

    public abstract Server getServer();

    public abstract void setServer(Server s);

    public enum UserType {
        //What kind of user is this?
        BEDROCK,
        JAVA
    }
}
