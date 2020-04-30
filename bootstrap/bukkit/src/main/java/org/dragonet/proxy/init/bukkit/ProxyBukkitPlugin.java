/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.init.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.util.PlatformType;
import org.dragonet.proxy.util.TextFormat;

import java.io.File;

public class ProxyBukkitPlugin extends JavaPlugin {
    private DragonProxy proxy;

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.AQUA + "Starting DragonProxy Bukkit...");

        // Create data folder
        getDataFolder().mkdir();

        // Initialize the main proxy class
        proxy = new DragonProxy(PlatformType.BUKKIT, getDataFolder());
    }

    @Override
    public void onDisable() {
        proxy.shutdown();
    }
}
