## Welcome to DragonProxy wiki

### 1. Presentation
DragonProxy is the only Minecraft: Bedrock Edition <=> Minecraft: Java Edition bridge, based on protocol translation.<br />
With this proxy, PE players (W10, Android / IOS) can join PC servers.<br />
This is probably the future of Minecraft servers, as Microsoft does not provide featured PE server publicly !

### 2. How it works
Here is a small description of the protocol translator :<br />

- 1. [JRakNet](https://github.com/JRakNet/JRakNet)<br />
This is a java based Raknet protocole implementation.

- 2. [RaknetInterface](../../../blob/master/proxy/src/main/java/org/dragonet/proxy/network/RaknetInterface.java)<br />
This is a light Minecraft PE server.

- 3. [UpStreamSession](../../../blob/master/proxy/src/main/java/org/dragonet/proxy/network/UpStreamSession.java)<br />
This class manage the PE packets listener / sender.

- 4. [PacketTranslator + cache](Packets.md))<br />
The packet translator PE <=> PC with caching for later use of the data.

- 5. [DownStreamSession](../../../blob/master/proxy/src/main/java/org/dragonet/proxy/network/PCDownstreamSession.java)<br />
This class manage the PC packets listener / sender.

- 6. [MCProtocolLib](https://github.com/Steveice10/MCProtocolLib)<br />
This library is used to create a Minecraft PC client for connecting to PC servers.

### 3. Features
- [x] Connect
- [x] Load chunks
- [x] Chat / commands
- [x] Move
- [ ] Inventory
- [ ] Entities
- [ ] Damages
- [ ] Interact

### 4. Future
- Online mode (auth gui based)
- Scoreboard

### 5. Compile
To compile, simply run in proxy module : ```mvn package``` or Compile in IDE

### 6. Resources
[Wiki.vg](http://wiki.vg/)<br />
[MiNET](https://github.com/NiclasOlofsson/MiNET/)<br />
[Nukkit](https://github.com/Nukkit/Nukkit)