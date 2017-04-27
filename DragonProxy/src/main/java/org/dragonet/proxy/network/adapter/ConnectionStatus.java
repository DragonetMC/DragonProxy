package org.dragonet.proxy.network;

public enum ConnectionStatus {

	UNCONNECTED, // First step
	AWAITING_CLIENT_AUTHENTICATION, // Wating for the client to send the data used for online and cls mode
	AWAITING_CLIENT_LOGIN, // Waiting for the login packet
	CONNECTED;
}
