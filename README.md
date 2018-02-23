# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
Travis: [![TravisCI](https://travis-ci.org/DragonetMC/DragonProxy.svg?branch=master)](https://travis-ci.org/DragonetMC/DragonProxy)
CodeMC: [![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonProxy)](https://ci.codemc.org/job/DragonProxy/)

A proxy for to allow **Minecraft: Bedrock** clients to connect to **Minecraft: Java Edition** servers.

## What is DragonProxy
DragonProxy is a software placed between a Minecraft Bedrock client and a Java Minecraft server.

## What DragonProxy is NOT
DragonProxy is __NOT a plugin__, __NOT a server__ like Spigot or Bungeecord, it's __NOT a BOT__, __NOT a CHEAT__ (it's sometimes buggy but we are working on this).

## Warning
__Some servers anticheats trigger some movments cheat, use at your own risks.__
If you get banned from a server, we do not take responsabilities.

## Features
- Currently supporting Bedrock 1.2.10 and Java 1.12.2
- Chat supported
- Chunks supported
- Movement supported
- Block break / place supported
- Entities spawn move and equipment
- BocksEntities spawn
- Inventory WIP

## Known bugs :
 - No chunk updates on world changing / teleport to far
 - No creative inventory
 - Movments on stairs/half blocks
 - Some anticheat trigger bad movments

## Won't fix/implements :
 - Older protocol compatibility (including plugins like ViaVersion)

![Screenshot](https://github.com/DragonetMC/DragonProxy/raw/master/screenshots/hypixel.png)

## Download
 - [release](https://github.com/DragonetMC/DragonProxy/releases) : stable builds, low features
 - [snapshots](https://ci.codemc.org/view/DragonetMC/job/DragonProxy/) : developments builds, unstable, latest features

## Compiling
 - clone from github
 - execute ```mvn package``` in root project dir

## Starting
 - to generate a config file, start with ```java -jar dragonproxy-x.x.x.jar``` (where x.x.x is the version)
 - stop the process with CTRL+C
 - edit [config.yml](https://github.com/DragonetMC/DragonProxy/blob/master/proxy/src/main/resources/config.yml) as needed
 - then start again

## Windows 10 localhost problem
If you try to run DragonProxy on the __same computer__ you start Minecraft W10 Edition, you must [enable loopback for Minecraft protocol first](http://pmmp.readthedocs.io/en/rtfd/faq/connecting/win10localhostcantconnect.html).

## Supported Remote Server Types
- For Java Edition Servers: `Bukkit`/`Spigot`/`Glowstone`/`SpongeVanilla`/`BungeeCord`
- Not listed does **NOT** mean unsupported, just **untested**.

## Libraries used
* [JRakNet by the MarfGamer](https://github.com/JRakNet/JRakNet)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

## Thanks :
* [PocketMine-MP](https://github.com/pmmp/PocketMine-MP)
* [Nukkit](https://github.com/NukkitX/Nukkit)
* [MINET](https://github.com/NiclasOlofsson/MiNET)
* [MinecraftCoalition](http://wiki.vg)