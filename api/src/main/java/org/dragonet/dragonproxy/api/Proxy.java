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
package org.dragonet.dragonproxy.api;

import org.dragonet.dragonproxy.api.command.CommandSource;

import java.nio.file.Path;

public interface Proxy {

    String getVersion();
    Path getFolder();
    void shutdown();
    CommandSource getConsole();
}
