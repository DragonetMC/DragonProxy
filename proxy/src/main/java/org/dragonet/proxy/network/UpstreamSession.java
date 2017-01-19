/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

import org.dragonet.proxy.DesktopServer;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.PocketServer;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.RemoteServer;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.utilities.HTTP;
import org.dragonet.proxy.utilities.PatternChecker;
import org.dragonet.proxy.utilities.Versioning;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.auth.service.AuthenticationService;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.game.PlayerListEntry;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.FullChunkDataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.raknet.protocol.EncapsulatedPacket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession {

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final String raknetID;

    @Getter
    private final InetSocketAddress remoteAddress;

    @Getter
    private final PEPacketProcessor packetProcessor;

    private final ScheduledFuture<?> packetProcessorScheule;

    @Getter
    private String username;
    
    @Getter
    private ConnectionStatus status = ConnectionStatus.UNCONNECTED;

    @Getter
    private DownstreamSession downstream;

    /* =======================================================================================================
     * |                                 Caches for Protocol Compatibility                                   |
    /* ======================================================================================================= */
    @Getter
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>());

    @Getter
    private final Map<UUID, PlayerListEntry> playerInfoCache = Collections.synchronizedMap(new HashMap<UUID, PlayerListEntry>());

    @Getter
    private final EntityCache entityCache = new EntityCache(this);

    @Getter
    private final WindowCache windowCache = new WindowCache(this);
    
    /* ======================================================================================================= */
    private MinecraftProtocol protocol;
    
    private LoginPacket peServerLoginPacket = null;

    public UpstreamSession(DragonProxy proxy, String raknetID, InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        packetProcessor = new PEPacketProcessor(this);
        packetProcessorScheule = proxy.getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
        status = ConnectionStatus.AWAITING_CLIENT_LOGIN;
    }

    public void handlePacketBinary(EncapsulatedPacket pk) {
    	//TODO: Add a timeout system so that if the LoginPacket gets lost the client will be auto disconnected
    	if(getStatus() == ConnectionStatus.CONNECTED){
    		packetProcessor.putPacket(pk.buffer);    		
    	}
    	
    	DataPacket[] packets = Protocol.decode(pk.buffer);
    	
    	for(DataPacket packet : packets){
    		DragonProxy.getLogger().info(packet.getClass().getCanonicalName());
        	if(getStatus() == ConnectionStatus.UNCONNECTED || getStatus() == ConnectionStatus.AWAITING_CLIENT_LOGIN){
                if(packet.pid() == ProtocolInfo.LOGIN_PACKET){
                    try {
                    	onClientLoginRequest((LoginPacket) packet);
                    } catch (Exception e){
                    	e.printStackTrace();
                    }
                    return;
                }
        	} else if(getStatus() == ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION){
    	        if(packet.pid() == ProtocolInfo.TEXT_PACKET && getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null){
    	        	TextPacket pack = (TextPacket) packet;
    	            if (getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")) {
    	                if (!PatternChecker.matchEmail(pack.message.trim())) {
    	                	
    	                    sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
    	                    disconnect(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
    	                    return;
    	                }
    	                getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, pack.message.trim());
    	                getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
    	                sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
    	            } else if (getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")) {
    	                if (getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || pack.message.equals(" ")) {
    	                    sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
    	                    disconnect(getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
    	                    return;
    	                }
    	                sendChat(getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
    	                getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
    	                authenticateOnlineMode(pack.message); //We NEVER cache password for better security. 
    	            }
    	            return;
    	        }       		
        	}
    	}
    }
    
    public void onClientLoginRequest(LoginPacket packet) {
    	status = ConnectionStatus.CONNECTING_CLIENT;
        if (username != null) {
            disconnect("Already logged in, this must be an error! ");
            return;
        }

        PlayStatusPacket status = new PlayStatusPacket(); // Required; Tells the client that his connection was accepted or denied
        if (packet.protocol != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = (packet.protocol < Versioning.MINECRAFT_PE_PROTOCOL ? PlayStatusPacket.LOGIN_FAILED_CLIENT : PlayStatusPacket.LOGIN_FAILED_SERVER);
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }

        this.username = packet.username;
        this.peServerLoginPacket = packet;
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        
        switch(proxy.getAuthMode()){
        case "online":
        	// We must send enough packets to make chat accessable on the client
            minimalClientHandshake(false);
            this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
            dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
            break;
        case "cls":
        	this.status = ConnectionStatus.AWAITING_CLIENT_AUTHENTICATION;
        	authenticateCLSMode();
            break;
        case "offline":
        	// We translate everything we are sent without regard for what it is
        	minimalClientHandshake(false);
        	
            protocol = new MinecraftProtocol(username);
            proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getDefault_server() + "]... ");
            connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
            break;
        }
    }
    
    public void sendPacket(DataPacket packet) {
        sendPacket(packet, false);
    }

    public void sendPacket(DataPacket packet, boolean immediate) {
        proxy.getNetwork().sendPacket(raknetID, packet, immediate);
    }

    public void sendAllPackets(DataPacket[] packets, boolean immediate) {
        if (packets.length < 5) {
            for (DataPacket packet : packets) {
                sendPacket(packet);
            }
        } else {
        	System.err.println("Batch Packet 1");
            BatchPacket batch = new BatchPacket();
            boolean mustImmediate = immediate;
            if (!mustImmediate) {
                for (DataPacket packet : packets) {
                	sendPacket(packet, mustImmediate);
                	//TODO: Fix batch packet processing
/*                    if (true) {
                        batch.putByteArray(packet.getBuffer());
                        mustImmediate = true;
                        break;
                    }*/
                }
            }
            //sendPacket(batch, mustImmediate);
        }
    }

    public void sendChat(String chat) {
        if (chat.contains("\n")) {
            String[] lines = chat.split("\n");
            for (String line : lines) {
                sendChat(line);
            }
            return;
        }
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_SYSTEM;
        pk.source = "";
        pk.message = chat;
        sendPacket(pk, true);
    }

    public void sendPopup(String text) {
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_POPUP;
        pk.source = "";
        pk.message = text;
        sendPacket(pk, true);
    }

    public void sendFakeBlock(int x, int y, int z, int id, int meta) {
        UpdateBlockPacket pkBlock = new UpdateBlockPacket();
        pkBlock.flags = UpdateBlockPacket.FLAG_ALL;
        pkBlock.x = x;
        pkBlock.y = (byte) (y & 0xFF);
        pkBlock.z = z;
        pkBlock.blockId = (byte) (id & 0xFF);
        pkBlock.blockData = (byte) (meta & 0xFF);
        sendPacket(pkBlock, true);
    }
    
    public void onTick() {
        entityCache.onTick();
        if(downstream != null)
            downstream.onTick();
    }

    /**
     * Called when this client disconnects.
     *
     * @param reason The reason of disconnection.
     */
    public void onDisconnect(String reason) {
        proxy.getLogger().info(proxy.getLang().get(Lang.CLIENT_DISCONNECTED, proxy.getAuthMode().equals("cls") ? "unknown" : username, remoteAddress, reason));
        if (downstream != null) {
            downstream.disconnect();
        }
        proxy.getSessionRegister().removeSession(this);
        packetProcessorScheule.cancel(true);
    }

    public void onConnected() {
        status = ConnectionStatus.CONNECTED;
    }
    
    /**
     * Disconnected from server. 
     * @param reason 
     */
    public void disconnect(String reason) {
        if(status != ConnectionStatus.UNCONNECTED) {
            proxy.getNetwork().closeSession(raknetID, reason);
            status = ConnectionStatus.UNCONNECTED;
            //RakNet server will call onDisconnect()
        }
    }
    
    public void authenticateCLSMode(){
        //CLS LOGIN! 
        if ((username.length() < 6 + 1 + 1) || (!username.contains("_"))) {
        	// Disconnect the player if their username can't possibly be a valid cls mode name
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
            return;
        }
        String name = username.substring(0, username.length() - 7);
        String keyCode = username.substring(username.length() - 6);
        String resp = HTTP.performGetRequest("http://api.dragonet.org/cls/query_token.php?" + String.format("username=%s&keycode=%s", name, keyCode));
        if (resp == null) {
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)));
            proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_UNREACHABLE)).replace("§c", "").replace("§0", ""));
            return;
        }
        JsonElement json = null;
        try{
            JsonParser jsonParser = new JsonParser();
            json = jsonParser.parse(resp);
        }catch(Exception e){
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
            proxy.getLogger().severe(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)).replace("§c", "").replace("§0", ""));
            //Json parse error! 
            return;
        }
        JsonObject obj = json.getAsJsonObject();
        if(!obj.get("status").getAsString().equals("success")){
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_CLS_NOTICE));
            return;
        }
        AuthenticationService authSvc = new AuthenticationService(obj.get("client").getAsString());
        authSvc.setUsername(obj.get("ign").getAsString());
        authSvc.setAccessToken(obj.get("token").getAsString());
        try {
            authSvc.login();
        } catch (RequestException ex) {
            ex.printStackTrace();
            sendStartGameAndDisconnect(proxy.getLang().get(Lang.MESSAGE_SERVER_ERROR, proxy.getLang().get(Lang.ERROR_CLS_ERROR)));
            return;
        }
        username = authSvc.getSelectedProfile().getName();
        HTTP.performGetRequest("http://api.dragonet.org/cls/update_token.php?" + String.format("username=%s&oldtoken=%s&newtoken=%s", name, obj.get("token").getAsString(), authSvc.getAccessToken()));
        protocol = new MinecraftProtocol(authSvc.getSelectedProfile(), authSvc.getAccessToken());

        proxy.getLogger().debug("Initially joining [" + proxy.getConfig().getDefault_server() + "]... ");
        connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
    }
    
    public void authenticateOnlineMode(String password) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                protocol = new MinecraftProtocol((String) dataCache.get(CacheKey.AUTHENTICATION_EMAIL), password, false);
            } catch (RequestException ex) {
                if (ex.getMessage().toLowerCase().contains("invalid")) {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    return;
                } else {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return;
                }
            }

            if (!username.equals(protocol.getProfile().getName())) {
                username = protocol.getProfile().getName();
                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_USERNAME, username));
            }

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS, username));

            proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE, username, remoteAddress, username));
            connectToServer(proxy.getConfig().getRemote_servers().get(proxy.getConfig().getDefault_server()));
        });
    }
    
    public void sendStartGameAndDisconnect(String reason) {
        //Login error so player in nether (Red screen)
    	minimalClientHandshake(true);
        sendChat(reason);
        disconnect(reason);
    }
    
    private void minimalClientHandshake(boolean errorMode){
    	PlayStatusPacket status = new PlayStatusPacket(); // Required; TODO: Find out why
        status.status = PlayStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, true);
        
        sendPacket(new ResourcePacksInfoPacket(), true);  // Causes the client to switch to the "locating server" screen
        
        StartGamePacket startGamePacket = new StartGamePacket(); // Required; Makes the client switch to the "generating world" screen
        startGamePacket.entityUniqueId = 52;
        startGamePacket.entityRuntimeId = 52;
        startGamePacket.x = (float) 0.0;
        startGamePacket.y = (float) 72F;
        startGamePacket.z = (float) 0.0;
        startGamePacket.seed = 242540254;
        startGamePacket.dimension = (byte) ((errorMode ? 1 : 0) & 0xff);
        startGamePacket.gamemode = 0;
        startGamePacket.difficulty = 1;
        startGamePacket.spawnX = (int) 0.0;
        startGamePacket.spawnY = (int) 72;
        startGamePacket.spawnZ = (int) 0.0;
        startGamePacket.hasAchievementsDisabled = true;
        startGamePacket.dayCycleStopTime = -1;
        startGamePacket.eduMode = false;
        startGamePacket.rainLevel = 0;
        startGamePacket.lightningLevel = 0;
        startGamePacket.commandsEnabled = true;
        startGamePacket.levelId = "";
        startGamePacket.worldName = ""; // Must not be null or a NullPointerException will occur
        startGamePacket.generator = 1; //0 old, 1 infinite, 2 flat
        sendPacket(startGamePacket, true);

        SetSpawnPositionPacket pkSpawn = new SetSpawnPositionPacket();
        pkSpawn.x = 0;
        pkSpawn.y = 72;
        pkSpawn.z = 0;
        sendPacket(pkSpawn, true);
        
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = 52;
        pkMovePlayer.x = (float) 0;
        pkMovePlayer.y = (float) 72;
        pkMovePlayer.z = (float) 0;
        pkMovePlayer.headYaw = 0.0f;
        pkMovePlayer.yaw = 0.0f;
        pkMovePlayer.pitch = 0.0f;
        pkMovePlayer.onGround = false;
        pkMovePlayer.mode = MovePlayerPacket.MODE_RESET;
        sendPacket(pkMovePlayer, true);
        
        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.time = 1000;
        setTimePacket.started = true;
        sendPacket(setTimePacket, true);
        
        SetDifficultyPacket pkSetDiff = new SetDifficultyPacket();
        pkSetDiff.difficulty = 1;
        sendPacket(pkSetDiff, true);
        
        AdventureSettingsPacket pkAdventureSettings = new AdventureSettingsPacket();
        pkAdventureSettings.allowFlight = true;
        pkAdventureSettings.isFlying = false;
        pkAdventureSettings.flags = 4;
        sendPacket(pkAdventureSettings, true);
        
        UpdateAttributesPacket pkUpdateAttr = new UpdateAttributesPacket();
        Attribute.init();
        pkUpdateAttr.entries = new Attribute[] {
        		Attribute.getAttribute(Attribute.ABSORPTION),
        		Attribute.getAttribute(Attribute.SATURATION),
        		Attribute.getAttribute(Attribute.EXHAUSTION),
        		Attribute.getAttribute(Attribute.KNOCKBACK_RESISTANCE),
        		Attribute.getAttribute(Attribute.MAX_HEALTH),
        		Attribute.getAttribute(Attribute.MOVEMENT_SPEED),
        		Attribute.getAttribute(Attribute.FOLLOW_RANGE),
        		Attribute.getAttribute(Attribute.MAX_HUNGER),
        		Attribute.getAttribute(Attribute.ATTACK_DAMAGE),
        		Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL),
        		Attribute.getAttribute(Attribute.EXPERIENCE)
        };
        //sendPacket(pkUpdateAttr, true);
        
        CraftingDataPacket pkCraftData = new CraftingDataPacket();
        pkCraftData.entries = Collections.EMPTY_LIST;
        sendPacket(pkCraftData, true);
        
        SetEntityDataPacket pkEntityData = new SetEntityDataPacket();
        pkEntityData.eid = 52;
        pkEntityData.metadata = new EntityMetadata();
        sendPacket(pkEntityData, true);
        
        ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
        containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
        containerSetContentPacket.slots = Item.getCreativeItems().stream().toArray(Item[]::new);
        sendPacket(containerSetContentPacket, true);
        
        sendFlatChunks(0, 0, 20, true);
        sendFlatChunks(0, 0, 17, false);
        
        ChunkRadiusUpdatedPacket pkChunkRadius = new ChunkRadiusUpdatedPacket();
        pkChunkRadius.radius = 3;
        sendPacket(pkChunkRadius, true);
        
/*        
        
        SetCommandsEnabledPacket pk = new SetCommandsEnabledPacket();
        pk.enabled = true;
        sendPacket(pk, true);
        
*/
        
        RespawnPacket pkResp = new RespawnPacket();
        pkResp.y = 72F;
        sendPacket(pkResp, false);
        
        PlayStatusPacket pkStat = new PlayStatusPacket(); //Required; Spawns the client in the world and closes the loading screen
        pkStat.status = PlayStatusPacket.PLAYER_SPAWN;
        sendPacket(pkStat, true);
    }

    private void sendFlatChunks(int playerX, int playerZ, int circleRadius, boolean sendAir) {
		int blocksX = 1;
		int blocksZ = 1;

		boolean cx = false; // Centered?
		// float circleRadius = player.getRenderDistance(); // Circle
		// Radius
		int maxBlocksX, maxBlocksZ;

		if (!cx) {
			maxBlocksX = (int) (Math.ceil((circleRadius - blocksX / 2) / blocksX) * 2 + 1);
			maxBlocksZ = (int) (Math.ceil((circleRadius - blocksZ / 2) / blocksZ) * 2 + 1);
		} else {
			maxBlocksX = (int) (Math.ceil(circleRadius / blocksX) * 2);
			maxBlocksZ = (int) (Math.ceil(circleRadius / blocksZ) * 2);
		}

		// TODO: Cache the chunk circle
		// Calculate the chunk ring
		//ArrayList<Vector3D> loadChunksList = new ArrayList<>();
		for (int z = -maxBlocksZ / 2; z <= maxBlocksZ / 2; z++) {
			for (int x = -maxBlocksX / 2; x <= maxBlocksX / 2; x++) {
				double distance = Math.sqrt(Math.pow(z * blocksZ, 2) + Math.pow(x * blocksX, 2));
				boolean shouldSendChunk = (distance < circleRadius);
				if (shouldSendChunk) {
					sendPacket(getFlatChunkPacket(x, z, (sendAir ? 0 : 1)), true);
					//loadChunksList.add(new Vector3D(nx - x, 0, nz - z));
				}
			}
		}
	}

	private DataPacket getFlatChunkPacket(int chunkX, int chunkZ, int blockId) {
		FullChunkDataPacket pePacket = new FullChunkDataPacket();
		
		cn.nukkit.level.format.mcregion.Chunk chunk = cn.nukkit.level.format.mcregion.Chunk.getEmptyChunk(chunkX, chunkZ);
		
		pePacket.chunkX = chunkX;
		pePacket.chunkZ = chunkZ;
		
		for(int yPC = 0; yPC < 128; yPC++){
			for(int x = 0; x < 16; x++){
				for(int z = 0; z < 16; z++){
					chunk.setBlockId(x, yPC, z, blockId);
					chunk.setBlockData(x, yPC, z, 0);
					chunk.setBlockLight(x, yPC, z, 15);
					chunk.setBlockSkyLight(x, yPC, z, 15);
					chunk.setBiomeId(x, z, 0);
				}
			}
		}
		
		pePacket.data = chunk.toFastBinary();
		return pePacket;
	}

	public void connectToServer(RemoteServer server){
        if(server == null) return;
        status = ConnectionStatus.CONNECTING_SERVER;
        if(downstream != null && downstream.isConnected()){
            downstream.disconnect();
            // TODO: Send chat message about server change. 
            
            // Remove all loaded entities
            BatchPacket batch = new BatchPacket();
            this.entityCache.getEntities().entrySet().forEach((ent) -> {
                if(ent.getKey() != 0){
                	RemoveEntityPacket pkRemoveEntity = new RemoveEntityPacket();
                	pkRemoveEntity.eid = ent.getKey();
                	sendPacket(pkRemoveEntity, true);
                    //batch.packets.add(pkRemoveEntity);
                }
            });
            this.entityCache.reset(true);
            sendPacket(batch, true);
            return;
        }
        
        if(server.getClass().isAssignableFrom(DesktopServer.class)){
            downstream = new PCDownstreamSession(proxy, this);
            ((PCDownstreamSession)downstream).setProtocol(protocol);
            ((PCDownstreamSession)downstream).connect(server.getRemoteAddr(), server.getRemotePort());
        }else{
            downstream = new PEDownstreamSession(proxy, this, peServerLoginPacket);
            ((PEDownstreamSession)downstream).connect((PocketServer) server);
        }
    }
}
