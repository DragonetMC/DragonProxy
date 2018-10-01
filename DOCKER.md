## Docker mode for DragonProxy

# Build the image
```./docker.sh```

# Start the conainer
```
docker run --rm -ti -p 19132:19132 -p 19132:19132/udp dragonet/dragonproxy:<version>
```

# Environment variables (override configuration)
 - MOTD the message in ping status
 - MAX_PLAYERS proxy max players
 - TARGET_SERVER minecraft server to point to (default mc.hypixel.net:25565)
 - AUTH_MODE proxy auth mode (online, offline, cls)
 - VERIFY check if XboxLive accounts are signed
 - CLS_SERVER the CLS server to contact in CLS mode
 - THREADS thread pool size
 - DP_SENTRY_CLIENT_KEY the sentry DSN client key
 - DP_DEBUG_LOGGING debug profiles login

# Volumes
 - /home/proxy/logs external logs directory
 - /home/proxy/config.yml override for proxy config file
