![Logo](http://dragonet.org/assets/img/Dragonet.png)

A proxy for **Minecraft - Pocket Edition** connecting to **Minecraft PC/Mac** servers.

Video by **[@MrPowerGamerBR](https://github.com/MrPowerGamerBR)** (On an older version): https://www.youtube.com/watch?v=oRSKQMzZYDE

![Screenshot](https://raw.githubusercontent.com/DragonetMC/DragonProxy/master/screenshots/TheArchon.png)

Wanna donate to help us? Go to http://dragonet.org/donate.php

## Installation
[![Build Status](https://drone.io/github.com/DragonetMC/DragonProxy/status.png)](https://drone.io/github.com/DragonetMC/DragonProxy/files) [Download](https://drone.io/github.com/DragonetMC/DragonProxy/files)
Once compiled, run `dragonproxy-*.jar` in `proxy/target` where '*' is the version number.

## Compiling
See the tutorial in the [wiki](https://github.com/DragonetMC/DragonProxy/wiki).
Note: Maven won't build the project because every class in DragonProxy/DragonProxy/src/main/java/org/dragonet/proxy/network/translator/pe and DragonProxy/DragonProxy/src/main/java/org/dragonet/proxy/network/translator/pc has compile errors. Just move these folders out of the project to fix the problem until the translators are updated

#### Supported Remote Server Types
- For PC/Mac Servers: `Bukkit`/`Spigot`/`Glowstone`/`SpongeVanilla`/`BungeeCord`
- For Pocket Edition Servers: `MiNET`, `Nukkit`, `PocketMine-MP (and all forks)`
- Not listed does **NOT** mean unsupported, just **untested**.

## Community
**Website:** [http://dragonet.org](http://dragonet.org)  
**Forums:** [http://forums.dragonet.org](http://forums.dragonet.org)

## Libraries Used
* RakLib Port by the Nukkit project @ [Site](http://nukkit.io)
* @Steveice10 's MCProtocolLib @ [Link](https://github.com/Steveice10/MCProtocolLib)
