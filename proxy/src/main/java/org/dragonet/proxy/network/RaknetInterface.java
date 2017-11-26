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
package org.dragonet.proxy.network;

import java.net.InetSocketAddress;
import java.util.*;

import net.marfgamer.jraknet.RakNetPacket;
import net.marfgamer.jraknet.identifier.MinecraftIdentifier;
import net.marfgamer.jraknet.server.RakNetServer;
import net.marfgamer.jraknet.server.RakNetServerListener;
import net.marfgamer.jraknet.server.ServerPing;
import net.marfgamer.jraknet.session.RakNetClientSession;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.protocol.ProtocolInfo;
import org.dragonet.proxy.utilities.Versioning;

public class RaknetInterface implements RakNetServerListener {
	// vars
	public static final Set<String> IMMEDIATE_PACKETS = new HashSet<>();

	private final DragonProxy proxy;
	private final SessionRegister sessions;
	private final RakNetServer rakServer;

	static {
		IMMEDIATE_PACKETS.add("PlayStatus");
	}

	// constructor
	public RaknetInterface(DragonProxy proxy, String ip, int port) {
		this.proxy = proxy;
		rakServer = new RakNetServer(port, Integer.MAX_VALUE);
		rakServer.addListener(this);
		rakServer.addSelfListener();
		sessions = this.proxy.getSessionRegister();
		rakServer.startThreaded();
	}

	// public
	public DragonProxy getProxy() {
		return proxy;
	}

	public RakNetServer getRakServer() {
		return rakServer;
	}

	/*
	 * public void onTick() { }
	 */

	public void handlePing(ServerPing ping) {
		System.out.println("PING " + ping.getSender().toString());
	}

	public void handleMessage(RakNetClientSession session, RakNetPacket packet, int channel) {
		UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
		if (upstream == null) {
			return;
		}
		// System.out.println("Received RakNet packet: " +
		// packet.getClass().getSimpleName());
		upstream.handlePacketBinary(packet.array());
	}

	public void onClientConnect(RakNetClientSession session) {
		System.out.println("CLIENT CONNECT");
		String identifier = session.getAddress().toString();
		UpstreamSession upstream = new UpstreamSession(proxy, identifier, session, session.getAddress());
		sessions.newSession(upstream);
	}

	public void onClientDisconnect(RakNetClientSession session, String reason) {
		System.out.println("CLIENT DISCONNECT");
		UpstreamSession upstream = sessions.getSession(session.getAddress().toString());
		if (upstream == null) {
			return;
		}
		upstream.onDisconnect(proxy.getLang().get(Lang.MESSAGE_CLIENT_DISCONNECT)); // It will handle rest of the
																					// things.
	}

	public void onThreadException(Throwable throwable) {
		System.out.println("Thread exception: " + throwable.getMessage());
		throwable.printStackTrace();
	}

	public void onHandlerException(InetSocketAddress address, Throwable throwable) {
		System.out.println("Handler exception: " + throwable.getMessage());
		throwable.printStackTrace();
	}

	public void onSessionException(RakNetClientSession session, Throwable throwable) {
		System.out.println("Session exception: " + throwable.getMessage());
		throwable.printStackTrace();
	}

	public void setBroadcastName(String serverName, int players, int maxPlayers) {
		rakServer.setIdentifier(
				new MinecraftIdentifier(serverName, ProtocolInfo.CURRENT_PROTOCOL, ProtocolInfo.MINECRAFT_VERSION_NETWORK,
						players, maxPlayers, new Random().nextLong(), "DragonProxy", "Survival"));
		if (!rakServer.isBroadcastingEnabled()) {
			rakServer.setBroadcastingEnabled(true);
		}
	}

	public void shutdown() {
		rakServer.shutdown();
	}

	// private

}
