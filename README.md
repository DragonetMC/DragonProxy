# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)

A proxy made to allow **Minecraft: Bedrock Edition** clients to connect to **Minecraft: Java Edition** servers.

### What is DragonProxy
DragonProxy is a proxy between a Minecraft: Bedrock Edition client and a Minecraft: Java Edition server.

### What's been done so far
The project has been undergoing a rewrite for some time, and development has recently picked up again.

- [x] RakNet broadcasting
- [x] Joining the remote server
- [x] Chat and commands support
  - [ ] Proper translation support
- [ ] Chunk loading
- [ ] Player movement
- [ ] Inventory support
- [ ] Viewing entities
- [x] Effects
- [ ] Block breaking / placing
- [x] Authentication (online mode)
- [x] Titles
- [x] Bossbar
- [ ] Scoreboard
- [ ] Statistics viewing
  - [x] General
  - [ ] Items
  - [ ] Mobs
- [ ] Backwards compatiblity
  - [ ] Bedrock
  - [ ] Java (may require a MCProtocolLib fork or wrapper)

A more comprehensive TODO list will be created in an issue in the future.

### Download
No updated releases are currently available.
 - [snapshots](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/): developments builds, unstable, latest features (recommended)

### Compiling
Clone the repo recursively and then run `mvn clean install`. The output jar will can be found at `bootstrap/target/DragonProxy.jar`.

### Libraries used
* [NukkitX Protocol Library](https://github.com/NukkitX/Protocol)
* [MCProtocolLib by Steveice10](https://github.com/Steveice10/MCProtocolLib)

  
[![HitCount](http://hits.dwyl.io/DragonetMC/DragonProxy.svg)](http://hits.dwyl.io/DragonetMC/DragonProxy)
 