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

import org.spacehq.mc.protocol.MinecraftConstants;

public class Versioning {

    public final static int VERSION_INT = 007; // TODO: Remove after checking theres no conflicts
    public final static String RELEASE_VERSION = "0.0.7";

    public final static String MINECRAFT_PC_VERSION = MinecraftConstants.GAME_VERSION;
    public final static int MINECRAFT_PC_PROTOCOL = MinecraftConstants.PROTOCOL_VERSION;

    // This is STRICT to MCPE binary's definition, DO NOT CHANGE
    public final static String MINECRAFT_PE_VERSION = "1.0.2";
    
    public final static int MINECRAFT_PE_PROTOCOL = 101;

}
