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
package org.dragonet.proxy.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RemoteServer implements IConfigurationSerializable {
	// vars
	public String remoteAddr;
	public int remotePort;

	// constructor
	public RemoteServer() {
		super();
	}

	// public
	/**
	 * Required for deserailization.
	 * 
	 * @param server
	 * @param map
	 * @return
	 */
	public static RemoteServer delicatedDeserialize(RemoteServer server, Map<String, Object> map) {
		server.remoteAddr = (String) map.get("remote_addr");
		server.remotePort = ((Number) map.get("remote_port")).intValue();
		return server;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("remote_addr", remoteAddr);
		map.put("remote_port", remotePort);
		return map;
	}

	// private

}
