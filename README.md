# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)
[![HitCount](http://hits.dwyl.io/DragonetMC/DragonProxy.svg)](http://hits.dwyl.io/DragonetMC/DragonProxy)


[![Generic badge](https://img.shields.io/badge/Bedrock-1.12-green.svg)](https://minecraft.gamepedia.com/Bedrock_Edition)
[![Generic badge](https://img.shields.io/badge/Java-1.14.4-green.svg)](https://minecraft.gamepedia.com/Java_Edition_1.14.4)

A proxy made to allow **Minecraft: Bedrock Edition** clients to connect to **Minecraft: Java Edition** servers.

### What is DragonProxy
DragonProxy is a proxy between a Minecraft: Bedrock Edition client and a Minecraft: Java Edition server.

### What's been done so far
The project has been undergoing a rewrite for some time, and development has recently picked up again.

- [x] RakNet broadcasting
- [x] Joining the remote server
- [x] Chat and commands support
  - [ ] Proper translation support
- [x] Chunk loading
- [ ] Player movement
- [ ] Inventory support
  - [x] Item NBT translation (name and lore, more to come soon)
- [ ] Viewing entities
- [x] Effects
- [ ] Block breaking / placing
- [x] Authentication (online mode)
- [x] Fetching skins
- [x] Titles
- [x] Bossbar
- [ ] Scoreboard
- [ ] Statistics viewing (./stats)
  - [x] General
  - [ ] Items
  - [ ] Mobs

A more comprehensive TODO list will be created in an issue in the future.

### Download
No updated releases are currently available.
 - [snapshots](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/): developments builds, unstable, latest features (recommended)

### Compiling
Clone the repo recursively and then run `mvn clean install`. The output jar will can be found at `bootstrap/target/DragonProxy.jar`.


### Donating
Donating helps support the project and its developers.  
  
You can donate at [OpenCollective](https://opencollective.com/DragonetMC) or [Patreon](https://patreon.com/DragonetMC). The latter is easier for us, however, and will give you automatic Discord perks.

### Libraries used
* [NukkitX Protocol Library](https://github.com/NukkitX/Protocol)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

CircleCI: [![CircleCI](https://circleci.com/gh/DragonetMC/DragonProxy.svg?style=svg)](https://circleci.com/gh/DragonetMC/DragonProxy)

###### *The REAL Better Together*
