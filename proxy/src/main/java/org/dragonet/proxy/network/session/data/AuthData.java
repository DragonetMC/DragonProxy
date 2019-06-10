/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view the LICENCE file for details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.data;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthData {
    private final String displayName;
    private final UUID identity;
    private final String xuid;

    public AuthData(String displayName, String identity, String xuid) {
        this.displayName = displayName;
        this.identity = UUID.fromString(identity);
        this.xuid = xuid;
    }
}
