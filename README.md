# DragonProxy

[![Financial Contributors on Open Collective](https://opencollective.com/dragonetmc/all/badge.svg?label=financial+contributors)](https://opencollective.com/dragonetmc) [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
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
- [ ] Chunk loading
- [ ] Player movement
- [ ] Inventory support
  - [x] Item NBT translation (name and lore, more to come soon)
- [ ] Viewing entities
- [x] Effects
- [ ] Block breaking / placing
- [x] Authentication (online mode)
- [x] Titles
- [x] Bossbar
- [ ] Scoreboard
- [ ] Statistics viewing (./stats)
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

CircleCI: [![CircleCI](https://circleci.com/gh/DragonetMC/DragonProxy.svg?style=svg)](https://circleci.com/gh/DragonetMC/DragonProxy)

## Contributors

### Code Contributors

This project exists thanks to all the people who contribute. [[Contribute](CONTRIBUTING.md)].
<a href="https://github.com/DragonetMC/DragonProxy/graphs/contributors"><img src="https://opencollective.com/dragonetmc/contributors.svg?width=890&button=false" /></a>

### Financial Contributors

Become a financial contributor and help us sustain our community. [[Contribute](https://opencollective.com/dragonetmc/contribute)]

#### Individuals

<a href="https://opencollective.com/dragonetmc"><img src="https://opencollective.com/dragonetmc/individuals.svg?width=890"></a>

#### Organizations

Support this project with your organization. Your logo will show up here with a link to your website. [[Contribute](https://opencollective.com/dragonetmc/contribute)]

<a href="https://opencollective.com/dragonetmc/organization/0/website"><img src="https://opencollective.com/dragonetmc/organization/0/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/1/website"><img src="https://opencollective.com/dragonetmc/organization/1/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/2/website"><img src="https://opencollective.com/dragonetmc/organization/2/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/3/website"><img src="https://opencollective.com/dragonetmc/organization/3/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/4/website"><img src="https://opencollective.com/dragonetmc/organization/4/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/5/website"><img src="https://opencollective.com/dragonetmc/organization/5/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/6/website"><img src="https://opencollective.com/dragonetmc/organization/6/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/7/website"><img src="https://opencollective.com/dragonetmc/organization/7/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/8/website"><img src="https://opencollective.com/dragonetmc/organization/8/avatar.svg"></a>
<a href="https://opencollective.com/dragonetmc/organization/9/website"><img src="https://opencollective.com/dragonetmc/organization/9/avatar.svg"></a>
