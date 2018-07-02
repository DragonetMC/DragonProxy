package org.dragonet.api.sessions;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import org.dragonet.api.ProxyServer;
import org.dragonet.api.caches.IChunkCache;
import org.dragonet.api.caches.IEntityCache;
import org.dragonet.api.caches.IJukeboxCache;
import org.dragonet.api.caches.IWindowCache;
import org.dragonet.api.network.PEPacket;
import org.dragonet.common.utilities.LoginChainDecoder;

/**
 * Represents the Bedrock edition session.
 */
public interface IUpstreamSession {

    /**
     * Returns the proxy instance.
     *
     * @return the proxy instance.
     */
    ProxyServer getProxy();

    /**
     * Returns the RaknetId.
     *
     * @return the RaknetId string.
     */
    String getRaknetID();

    //RakNetClientSession getRaknetClient();

    /**
     * Returns if the client is logged in.
     *
     * @return true if the client is logged in.
     */
    boolean isLoggedIn();

    /**
     * Returns true if the client is spawned in the map.
     *
     * @return true if the client is spawned.
     */
    boolean isSpawned();

    /**
     * Returns the client remote socket address.
     *
     * @return the client remote socket address.
     */
    InetSocketAddress getRemoteAddress();

    /**
     * Returns the packet processor.
     *
     * @return the packet processor.
     */
    IPEPacketProcessor getPacketProcessor();

    /**
     * Returns the client profile instance.
     *
     * @return the client profile instance.
     */
    LoginChainDecoder getProfile();

    /**
     * Returns the client username.
     *
     * @return the client username.
     */
    String getUsername();

    /**
     * Returns the associated downstream session.
     *
     * @return the Java side downstream session.
     */
    IDownstreamSession getDownstream();

    /**
     * Returns the session data cache.
     *
     * @return the session data cache.
     */
    Map<String, Object> getDataCache();

    /**
     * Returns the session player info cache.
     *
     * @return the session player info cache.
     */
    Map<UUID, PlayerListEntry> getPlayerInfoCache();

    /**
     * Returns the session entity cache.
     *
     * @return the session entity cache.
     */
    IEntityCache getEntityCache();

    /**
     * Returns the session inventory window cache.
     *
     * @return the session inventory window cache.
     */
    IWindowCache getWindowCache();

    /**
     * Returns the session map chunk cache.
     *
     * @return the session map chunk cache.
     */
    IChunkCache getChunkCache();

    /**
     * Returns the session jukebox cache.
     *
     * @return the session jukebox cache.
     */
    IJukeboxCache getJukeboxCache();

    /**
     * Returns the Java edition protocol library instance.
     *
     * @return the java edition protocol library instance.
     */
    MinecraftProtocol getProtocol();

    /**
     * Sends a packet to the Bedrock edition client.
     *
     * @param packet the Bedrock edition packet.
     * @param high_priority sends the packet immediately,
     *                      should be set to true when the packet needs to be handled before
     *                      the player spawns into the map.
     */
    void sendPacket(PEPacket packet, boolean high_priority);

    /**
     * Sends a packet to the Bedrock edition client.
     *
     * @param packet the Bedrock edition packet.
     */
    void sendPacket(PEPacket packet);

    /**
     * Sends packets to the Bedrock edition client.
     *
     * @param packets the Bedrock edition packets.
     * @param high_priority sends the packets immediately,
     *                      should be set to true when the packets need to be handled before
     *                      the player spawns into the map.
     */
    void sendAllPackets(PEPacket[] packets, boolean high_priority);

    /**
     * Connects the session to the specified Java edition server address.
     *
     * @param address the server address.
     * @param port the TCP server port number.
     */
    void connectToServer(String address, int port);

    /**
     * The client connection event handler.
     */
    void onConnected();

    /**
     * Disconnects the session from the current server.
     *
     * @param reason the reason to be sent to the client.
     */
    void disconnect(String reason);

    /**
     * Called when this client disconnects.
     *
     * @param reason the reason of disconnection.
     */
    void onDisconnect(String reason);

    /**
     * Performs the user authentication.
     *
     * @param email the account email.
     * @param password the account password.
     * @param authProxy the authentication proxy.
     */
    void authenticate(String email, String password, Proxy authProxy);

    /**
     * The login event handler.
     *
     * @param packet the login packet.
     */
    void onLogin(PEPacket packet);

    /**
     * The post login event handler.
     */
    void postLogin();

    /**
     * Marks the session as spawned.
     */
    void setSpawned();

    // TODO: documentation

    void sendChat(String chat);

    void sendFakeBlock(int x, int y, int z, int id, int meta);

    void sendCreativeInventory();

    void handlePacketBinary(byte[] packet);

    void putCachePacket(PEPacket packet);

    void onTick();
}
