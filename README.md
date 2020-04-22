# DragonProxy

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da.svg)](https://discord.gg/CmkxTz2)
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=DragonetMC/DragonProxy)](https://ci.codemc.org/job/DragonetMC/job/DragonProxy/)
[![HitCount](http://hits.dwyl.io/DragonetMC/DragonProxy.svg)](http://hits.dwyl.io/DragonetMC/DragonProxy)

A proxy made to allow **Minecraft: Bedrock Edition** clients to connect to **Minecraft: Java Edition** servers.

```
Bedrock 1.14.x // Java 1.15.2
```

## Stats
See live stats on BStats
[![Bstats](https://bstats.org/signatures/server-implementation/DragonProxy.svg)](https://bstats.org/plugin/server-implementation/DragonProxy/)

## What's been done so far
See the (almost) full list in [#464](https://github.com/DragonetMC/DragonProxy/issues/464).

  
## Setup
1. Download the latest build [on jenkins](https://ci.codemc.io/job/DragonetMC/job/DragonProxy/lastSuccessfulBuild/).  
2. Run DragonProxy with `java -jar DragonProxy.jar`  
3. Type 'stop' in console and edit the `config.yml` to your liking  

If you are having trouble please refer to our [Discord](https://invite.gg/DragonetMC).

  
## Compiling
### Prerequisites
* [Apache Maven](https://maven.apache.org/download.cgi)  
* Java 8+  
* Git bash  

### Instructions 
1. Clone the repo: `git clone --recursive https://github.com/DragonetMC/DragonProxy`  
2. Go to the repo folder `cd DragonProxy` (or whatever you called it) 
3. Execute `mvn clean package`  
  
> The output jar can be found at `bootstrap/standalone/target/DragonProxy.jar`.  

  
## Donating
Donating helps support the project. Read more on the [wiki](https://github.com/DragonetMC/DragonProxy/wiki/Donating).  
* [OpenCollective](https://opencollective.com/DragonetMC)  
* [Patreon](https://patreon.com/DragonetMC)  
.  
CircleCI: [![CircleCI](https://circleci.com/gh/DragonetMC/DragonProxy.svg?style=svg)](https://circleci.com/gh/DragonetMC/DragonProxy)
