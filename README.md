# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
Travis: [![TravisCI](https://travis-ci.org/DragonetMC/DragonProxy.svg?branch=master)](https://travis-ci.org/DragonetMC/DragonProxy)
CodeMC: [![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)

A proxy for to allow **Minecraft: Bedrock** clients to connect to **Minecraft: Java Edition** servers.

## What is DragonProxy
DragonProxy is a software placed between a Minecraft Bedrock client and a Java Minecraft server.

__Some servers anticheats trigger some movements cheat, use at your own risks.__
If you get banned from a server, we do not take responsabilities.

## Who uses DragonProxy
See live stats on BStats[![Bstats](https://bstats.org/signatures/server-implementation/DragonProxy.svg)](https://bstats.org/plugin/server-implementation/DragonProxy/)

 - If you want your server featured here, please ask on our Discord !

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
 - Some anticheat plugin trigger bad movements
 - You can get stuck in the ceiling
 - Switching worlds can have issues
 - No hit animation
 - Snowballs, Enderpearls and Arrows are buggy
 - Redstone can be buggy, but mostly works

![Screenshot](https://github.com/DragonetMC/DragonProxy/raw/master/screenshots/hypixel-2.png)

## Download
 - [release](https://github.com/DragonetMC/DragonProxy/releases) : stable builds
 - [snapshots](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/artifact/proxy/target/)(take first jar) : developments builds, unstable, latest features

## Compiling
You will need Java 8 JDK. You can download this from oracle's website or from your operating systems package manager.

You will also need Maven, which you can download from [here](http://maven.apache.org/download.cgi). Then make sure maven is in your `PATH` (theres plenty of tutorials online).

#### Windows
Install [git scm](https://git-scm.com/downloads) and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn package`.

If all goes well, the DragonProxy jar can be found in `proxy\target`. Enjoy!

#### Linux
Install `git` and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn package`.

If all goes well, the DragonProxy jar can be found in `proxy/target`. Enjoy!

## Installing
Download in a sepate directory, then:
 - to generate a config file, start with ```java -jar dragonproxy-x.x.x.jar``` (where x.x.x is the version)
 - stop the process with CTRL+C
 - edit [config.yml](https://github.com/DragonetMC/DragonProxy/blob/master/proxy/src/main/resources/config.yml) as needed
 - then start again

## Plugin setup
[DragonProxy plugins](https://github.com/DragonetMC/DragonProxy/tree/master/plugin) are optionnals. Warning : the hybrid auth is only implemented for bungeecord.

The [bungeecord](https://github.com/DragonetMC/DragonProxy/tree/master/plugin/bungee) plugin is used to auth players in hybrid auth mode.

The [bukkit](https://github.com/DragonetMC/DragonProxy/tree/master/plugin/spigot) plugin is used to send custom packets to the Bedrock client, but does not works yet.

## Windows 10 localhost problem
If you try to run DragonProxy on the __same computer__ you start Minecraft W10 Edition, you must [enable loopback for Minecraft protocol](http://pmmp.readthedocs.io/en/rtfd/faq/connecting/win10localhostcantconnect.html) first. **In the next release of DragonNet, this will no longer need to be done manually as the needed changes will be done through the code automatically. But for now, this section will stay until a release that does this on it's own is rolled out.**

## Libraries used
* [JRakNet by Whirvis](https://github.com/JRakNet/JRakNet)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

## Plugin developement (DragonProxy plugins)
A frenchy did a small plugin in a [video](https://www.youtube.com/playlist?list=PL1_LASKNkFJtc2q46yvD35EvraArSrICh)

The plugin API is not made, wait for it !

## Thanks :
* [PocketMine-MP](https://github.com/pmmp/PocketMine-MP)
* [Nukkit](https://github.com/NukkitX/Nukkit)
* [MINET](https://github.com/NiclasOlofsson/MiNET)
* [MinecraftCoalition](http://wiki.vg)
