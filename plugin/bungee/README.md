DragonProxy Bungeecord Plugin
=========================

This plugin allow XboxLive auth and Mojang auth to works at the same time.

WARNING : works only if Bungeecord if online mode !

Config :
 - auth_bypass_ip : a list of allowed incoming xboxlive IPs, meaning your DragonProxy server IP (127.0.0.1 if on the same host)

How it works :
- an ip filter is applyed to every incoming connection
- if the connection matches the filter, the plugin check the auth payload and process xboxlive auth, if not, mojang auth is checked
- if the xboxlive auth is completed, the players login likes a java one
- the xboxlive uid is used as an UUID, it's a different UUID version than Mojang one, so there are no UUID conflicts

You can also know if a player is a PE player or not with uour little api:
`boolean isBedrockPlayer = DPAddonBungee.getInstance().isBedrockPlayer(UUID uuid);`