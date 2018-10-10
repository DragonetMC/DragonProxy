# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
Travis: [![TravisCI](https://travis-ci.org/DragonetMC/DragonProxy.svg?branch=master)](https://travis-ci.org/DragonetMC/DragonProxy)
CodeMC: [![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)

A proxy made to allow **Minecraft: Bedrock** clients to connect to **Minecraft: Java Edition** servers.

## What is DragonProxy
DragonProxy is a proxy between a Minecraft Bedrock client and a Java Minecraft server.

__Some servers anticheats trigger some movements cheat, use at your own risks.__
If you get banned from a server, we do not take responsibilities.

## Who uses DragonProxy
See live stats on BStats[![Bstats](https://bstats.org/signatures/server-implementation/DragonProxy.svg)](https://bstats.org/plugin/server-implementation/DragonProxy/)

![Screenshot](https://github.com/DragonetMC/DragonProxy/raw/master/screenshots/hypixel-2.png)

## Download
 - [release](https://github.com/DragonetMC/DragonProxy/releases) : stable builds
 - [snapshots](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/artifact/proxy/target/): developments builds, unstable, latest features

## Compiling
You will need Java 8 JDK in order to compile the project. You can download it from Oracle's website or from your operating systems package manager.

You will also need Maven, which you can download from [here](http://maven.apache.org/download.cgi). Then make sure it is in your `PATH` environment variable.

#### Windows
Install [git scm](https://git-scm.com/downloads) and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn package`.

If all goes well, the DragonProxy jar can be found in the `proxy\target` directory.

#### Linux
Install `git` and run `git clone https://github.com/DragonetMC/DragonProxy`

Then run `cd DragonProxy` and `mvn clean package`.

If all goes well, the DragonProxy jar can be found in the `proxy/target` directory.

## Installing
Download the jar executable in a directory, then:
 - start the proxy with ```java -jar dragonproxy-x.x.x.jar``` (where x.x.x is the version)
 - wait for the proxy to start
 - stop the process with the CTRL+C hotkey
 - edit the default [config.yml](https://github.com/DragonetMC/DragonProxy/blob/master/proxy/src/main/resources/config.yml) file as needed
 - start again the proxy, enjoy!

## Libraries used
* [JRakNet by Whirvis](https://github.com/JRakNet/JRakNet)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

## Useful resources:
* [The NukkitX project](https://github.com/NukkitX/Nukkit)
* [The MINET project](https://github.com/NiclasOlofsson/MiNET)
* [The PocketMine-MP project](https://github.com/pmmp/PocketMine-MP)
* [The java protocol documentation](http://wiki.vg)
