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
package org.dragonet.proxy.utilities;

import org.dragonet.proxy.protocol.ProtocolInfo;

public class Versioning {
	// vars
	public static final String RELEASE_VERSION = "0.3.0";
	public static final String MINECRAFT_PC_VERSION = "1.12.2";
	// public static final int MINECRAFT_PC_PROTOCOL = 47;
	// This is STRICT to MCPE binary's definition, DO NOT CHANGE
	public static final String MINECRAFT_PE_VERSION = ProtocolInfo.MINECRAFT_VERSION;
	public static final int MINECRAFT_PE_PROTOCOL = ProtocolInfo.CURRENT_PROTOCOL;

	// constructor
	public Versioning() {

	}

	// public

	// private

}
