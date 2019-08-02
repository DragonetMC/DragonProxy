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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public final class AuthData {
    @JsonProperty
    private String displayName;
    @JsonProperty
    private UUID identity;
    @JsonProperty("XUID")
    @Setter
    private String xuid;
}
