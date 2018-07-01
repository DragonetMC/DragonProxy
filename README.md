# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
Travis: [![TravisCI](https://travis-ci.org/DragonetMC/DragonProxy.svg?branch=master)](https://travis-ci.org/DragonetMC/DragonProxy)
CodeMC: [![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)

A proxy which allows **Minecraft: Bedrock Edition** clients to connect to **Minecraft: Java Edition** servers.

## What is DragonProxy
DragonProxy is a proxy server placed between a Minecraft Bedrock client and a Java Minecraft server.

![Screenshot](https://github.com/DragonetMC/DragonProxy/raw/master/screenshots/hypixel-2.png)

__Some server anti-cheat plugin may trigger some warnings, use at your own risks.__
If you get banned from a server, we do not take any responsibility.

## Who uses DragonProxy
See live statistics on **BStats**[![Bstats](https://bstats.org/signatures/server-implementation/DragonProxy.svg)](https://bstats.org/plugin/server-implementation/DragonProxy/)

### Featured servers
If you want your server to be featured here, please ask on our Discord!

## Features
- Currently supporting Bedrock 1.4.2 and Java 1.12.2
- Chat supported
- Chunks supported
- Movement supported
- Block break / place supported
- Entities spawn move and equipment
- BocksEntities spawn
- Chests

## TODO List
See [#132](https://github.com/DragonetMC/DragonProxy/issues/132).

## Known bugs
 - No creative inventory
 - Movements on stairs/half blocks is weird
 - Some anti-cheat plugin trigger bad movements
 - You can get stuck in the ceiling
 - Switching worlds can have issues
 - No hit animation
 - Snowballs, Enderpearls and Arrows are buggy
 - Redstone can be buggy, but mostly works

## Download
 - [release](https://github.com/DragonetMC/DragonProxy/releases) : stable builds
 - [snapshots](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/artifact/proxy/target/)(take first jar) : developments builds, unstable, latest features

## Compiling
You will need Java 8 JDK. You can download this from oracle's website or from your operating systems package manager.

You will also need Maven, which you can download from [here](http://maven.apache.org/download.cgi). Then make sure maven is in your `PATH` (there are plenty of tutorials online).

#### Windows
Install [git scm](https://git-scm.com/downloads) and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn package`.

If all goes well, the DragonProxy jar can be found in `proxy\target`. Enjoy!

#### Linux
Install `git` and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn package`.

If all goes well, the DragonProxy jar can be found in `proxy/target`. Enjoy!

## Installing
 - download the desired proxy jar in a directory
 - start the proxy in order to generate the config file with ```java -jar dragonproxy-x.x.x.jar``` (where x.x.x is the version).
 - wait the startup process to complete then stop the proxy with the ```CTRL+C``` hotkey.
 - edit the [config.yml](https://github.com/DragonetMC/DragonProxy/blob/master/proxy/src/main/resources/config.yml) file as needed.
 - start the proxy again, enjoy.

## Windows 10 localhost problem
If you try to run DragonProxy on the __same computer__ of the Minecraft W10 Edition client, you must [enable loopback for Minecraft protocol](http://pmmp.readthedocs.io/en/rtfd/faq/connecting/win10localhostcantconnect.html) first.

## Plugin setup
[DragonProxy plugins](https://github.com/DragonetMC/DragonProxy/tree/master/plugin) are optionals.
Warning: the hybrid auth is only available for Bungeecord server environments.

The [bungeecord](https://github.com/DragonetMC/DragonProxy/tree/master/plugin/bungee) plugin is used to auth players in hybrid auth mode.

The [spigot](https://github.com/DragonetMC/DragonProxy/tree/master/plugin/spigot) plugin is used to send custom packets to the Bedrock client, **still a WIP**.

## Custom plugin development
French tutorial: [video](https://www.youtube.com/playlist?list=PL1_LASKNkFJtc2q46yvD35EvraArSrICh)

The plugin API is still a work in progress.

## Libraries used
* [JRakNet by Whirvis](https://github.com/JRakNet/JRakNet)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

## Thanks to:
* [PocketMine-MP](https://github.com/pmmp/PocketMine-MP) a PHP based Bedrock edition server.
* [Nukkit](https://github.com/NukkitX/Nukkit) a Java based Bedrock edition server.
* [MINET](https://github.com/NiclasOlofsson/MiNET) a C# based Bedrock edition server.
* [wiki.vg](http://wiki.vg) a Minecraft protocol specifications wiki.
