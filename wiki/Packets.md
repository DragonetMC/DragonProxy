## Packets translator

| PE Packet | ID (hex) | ID (dec) | Cached | Bound | PC Packet | ID (hex) | ID (dec) |
| --- |--- | --- | --- | --- | --- | --- | --- |
| Login | 0x01 | 1 |  |  |  |  |  |
| Play Status | 0x02 | 2 |  |  |  |  |  |
| Server To Client Handshake | 0x03 | 3 |  |  |  |  |  |
| Client To Server Handshake | 0x04 | 4 |  |  |  |  |  |
| Disconnect | 0x05 | 5 |  |  |  |  |  |
| Resource Packs Info | 0x06 | 6 |  |  |  |  |  |
| Resource Pack Stack | 0x07 | 7 |  |  |  |  |  |
| Resource Pack Client Response | 0x08 | 8 |  |  |  |  |  |
| Text | 0x09 | 9 |  | both | ClientChatPacket | 0x0F |  |
| Set Time | 0x0a | 10 |  | S => C | Time Update | 0x48 |  |
| Start Game | 0x0b | 11 |  | S => C | Join Game | 0x23 |  |
| Add Player | 0x0c | 12 |  | S => C | Spawn Player | 0x05 |  |
| Add Entity | 0x0d | 13 |  | S => C | Spawn Mob | 0x03 |  |
| Remove Entity | 0x0e | 14 |  | S => C | Destroy Entities | 0x32 |  |
| Add Item Entity | 0x0f | 15 |  | S => C | Collect Item | 0x4B |  |
| Take Item Entity | 0x11 | 17 |  |  |  |  |  |
| Move Entity | 0x12 | 18 |  | S => C | Entity Relative Move | 0x26 |  |
| Move Player | 0x13 | 19 |  | S => C | ClientPlayerPositionRotationPacket | 0x2F |  |
| Rider Jump | 0x14 | 20 |  |  |  |  |  |
| Update Block | 0x15 | 21 |  | S => C | Block Change | 0x0B |  |
| Add Painting | 0x16 | 22 |  | S => C | Spawn Painting | 0x04 |  |
| Explode | 0x17 | 2 |  |  |  |  |  |
| Level Sound Event | 0x18 | 24 |  |  |  |  |  |
| Level Event | 0x19 | 25 |  |  |  |  |  |
| Block Event | 0x1a | 26 |  | S => C | Block Action | 0x0A |  |
| Entity Event | 0x1b | 27 |  |  |  |  |  |
| Mob Effect | 0x1c | 28 |  | S => C | Entity Effect | 0x4F |  |
| Update Attributes | 0x1d | 29 |  |  |  |  |  |
| Inventory Transaction | 0x1e | 30 |  | C => S | Click Window | 0x07 |  |
| Mob Equipment | 0x1f | 31 |  | S => C | Entity Equipment | 0x3F |  |
| Mob Armor Equipment | 0x20 | 32 |  |  |  |  |  |
| Interact | 0x21 | 33 |  | C => S | Use Entity | 0x0A |  |
| Block Pick Request | 0x22 | 34 |  |  |  |  |  |
| Entity Pick Request | 0x23 | 35 |  |  |  |  |  |
| Player Action | 0x24 | 36 |  |  |  |  |  |
| Entity Fall | 0x25 | 37 |  |  |  |  |  |
| Hurt Armor | 0x26 | 38 |  |  |  |  |  |
| Set Entity Data | 0x27 | 39 |  | S => C | Entity Properties | 0x4E |  |
| Set Entity Motion | 0x28 | 40 |  |  |  |  |  |
| Set Entity Link | 0x29 | 41 |  | S => C | Set Passengers | 0x43 |  |
| Set Health | 0x2a | 42 |  | S => C | Update Health | 0x41 |  |
| Set Spawn Position | 0x2b | 43 |  | S => C | Spawn Position | 0x46 |  |
| Animate | 0x2c | 44 |  | S => C | Animation | 0x06 |  |
| Respawn | 0x2d | 45 |  | S => C | RespawnPacket | 0x35 |  |
| Container Open | 0x2e | 46 |  | S => C | Open Window | 0x13 |  |
| Container Close | 0x2f | 47 |  | S => C | Close Window | 0x12 |  |
| Player Hotbar | 0x30 | 48 |  |  |  |  |  |
| Inventory Content | 0x31 | 49 |  | S => C | Window Items | 0x14 |  |
| Inventory Slot | 0x32 | 50 |  | S => C | Set Slot | 0x16 |  |
| Container Set Data | 0x33 | 51 |  | S => C | Window Property | 0x15 |  |
| Crafting Data | 0x34 | 52 |  |  |  |  |  |
| Crafting Event | 0x35 | 53 |  |  |  |  |  |
| Gui Data Pick Item | 0x36 | 54 |  |  |  |  |  |
| Adventure Settings | 0x37 | 55 |  | S => C | Change Game State | 0x1E |  |
| Block Entity Data | 0x38 | 56 |  | S => C | Update Block Entity | 0x09 |  |
| Player Input | 0x39 | 57 |  | C => S | Steer Vehicle | 0x16 |  |
| Full Ch Data | 0x3a | 58 |  | S => C | Chunk Data | 0x20 |  |
| Set Commands Enabled | 0x3b | 59 |  |  |  |  |  |
| Set Difficulty | 0x3c | 60 |  | S => C | Server Difficulty | 0x0D |  |
| Change Dimension | 0x3d | 61 |  | S => C | RespawnPacket | 0x35 |  |
| Set Player Game Type | 0x3e | 62 |  |  |  |  |  |
| Player List | 0x3f | 63 |  | S => C | Player List Item | 0x2E |  |
| Simple Event | 0x40 | 64 |  |  |  |  |  |
| Telemetry Event | 0x41 | 65 |  |  |  |  |  |
| Spawn Experience Orb | 0x42 | 66 |  | S => C | Spawn Experience Orb | 0x01 |  |
| Clientbound Map Item Data  | 0x43 | 67 |  |  |  |  |  |
| Map Info Request | 0x44 | 68 |  |  |  |  |  |
| Request Ch Radius | 0x45 | 69 |  |  |  |  |  |
| Ch Radius Update | 0x46 | 70 |  |  |  |  |  |
| Item Frame Drop Item | 0x47 | 71 |  |  |  |  |  |
| Game Rules Changed | 0x48 | 72 |  |  |  |  |  |
| Camera | 0x49 | 73 |  | S => C | Camera | 0x39 |  |
| Boss Event | 0x4a | 74 |  | S => C | Boss Bar | 0x0C |  |
| Show Credits | 0x4b | 75 |  |  | Change Game State | 0x1E |  |
| Available Commands | 0x4c | 76 |  | S => C | Tab-Complete | 0x0E |  |
| Command Request | 0x4d | 77 |  |  |  |  |  |
| Command Block Update | 0x4e | 78 |  |  |  |  |  |
| Command Output | 0x4f | 79 |  |  |  |  |  |
| Update Trade | 0x50 | 80 |  |  |  |  |  |
| Update Equipment | 0x51 | 81 |  | S => C | Entity Equipment | 0x3F |  |
| Resource Pack Data Info | 0x52 | 82 |  |  |  |  |  |
| Resource Pack Ch Data | 0x53 | 83 |  |  |  |  |  |
| Resource Pack Ch Request | 0x54 | 84 |  |  |  |  |  |
| Transfer | 0x55 | 85 |  |  |  |  |  |
| Play Sound | 0x56 | 86 |  | S => C | Sound Effect | 0x49 |  |
| Stop Sound | 0x57 | 87 |  |  |  |  |  |
| Set Title | 0x58 | 88 |  | S => C | Title | 0x48 |  |
| Add Behavior Tree | 0x59 | 89 |  |  |  |  |  |
| Structure Block Update | 0x5a | 90 |  |  |  |  |  |
| Show Store Offer | 0x5b | 91 |  |  |  |  |  |
| Purchase Receipt | 0x5c | 92 |  |  |  |  |  |
| Player Skin | 0x5d | 93 |  |  |  |  |  |
| Sub Client Login | 0x5e | 94 |  |  |  |  |  |
| Initiate Web Socket Connection | 0x5f | 95 |  |  |  |  |  |
| Set Last Hurt By | 0x60 | 96 |  |  |  |  |  |
| Book Edit | 0x61 | 97 |  | C => S | Crafting Book Data | 0x17 |  |
| Npc Request | 0x62 | 98 |  |  |  |  |  |
| Modal Form Request | 0x64 | 100 |  |  |  |  |  |
| Modal Form Response | 0x65 | 101 |  |  |  |  |  |
| Server Settings Request | 0x66 | 102 |  |  |  |  |  |
| Server Settings Response | 0x67 | 103 |  |  |  |  |  |