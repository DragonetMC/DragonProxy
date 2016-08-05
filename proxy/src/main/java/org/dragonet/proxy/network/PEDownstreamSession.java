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

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.Getter;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.protocol.packet.BatchPacket;
import org.dragonet.proxy.protocol.packet.ChatPacket;
import org.dragonet.proxy.protocol.packet.LoginPacket;
import org.dragonet.proxy.protocol.packet.MovePlayerPacket;
import org.dragonet.proxy.protocol.packet.PEPacket;
import org.dragonet.proxy.protocol.packet.SetSpawnPositionPacket;
import org.dragonet.proxy.protocol.packet.StartGamePacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.PocketServer;
import org.dragonet.proxy.utilities.Binary;
import org.dragonet.proxy.utilities.DefaultSkin;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.client.ClientHandler;
import org.dragonet.raknet.client.ClientInstance;
import org.dragonet.raknet.client.JRakLibClient;
import org.dragonet.raknet.protocol.EncapsulatedPacket;

public class PEDownstreamSession implements DownstreamSession<PEPacket>, ClientInstance {

    public final static String ENTITY_ID_KEY = "ENTITYID";
    
    private JRakLibClient client;
    private ClientHandler handler;

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final UpstreamSession upstream;

    private PocketServer serverInfo;
    private boolean connected;

    public PEDownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }
    
    @Override
    public void onTick(){
        if(handler != null){
            while(handler.handlePacket()){}
        }
    }

    public void connect(PocketServer serverInfo) {
        this.serverInfo = serverInfo;
        connect(serverInfo.getRemoteAddr(), serverInfo.getRemotePort());
    }

    @Override
    public void connect(String addr, int port) {
        System.out.println("[" + upstream.getUsername() + "] Connecting to remote pocket server at [" + String.format("%s:%s", addr, port) + "] ");
        if (client != null) {
            upstream.onConnected(); // Clear the flags
            upstream.disconnect("ERROR! ");
        }
        client = new JRakLibClient(Logger.getLogger(upstream.getRemoteAddress().toString() + "<->" + serverInfo.toString()), addr, port);
        handler = new ClientHandler(client, this);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void send(PEPacket packet) {
        sendPacket(packet, true);
    }

    @Override
    public void send(PEPacket... packets) {
        for (PEPacket packet : packets) {
            sendPacket(packet, true);
        }
    }

    @Override
    public void sendChat(String chat) {
        ChatPacket pk = new ChatPacket();
        pk.type = ChatPacket.TextType.CHAT;
        pk.source = "";
        pk.message = chat;
        send(pk);
    }

    @Override
    public void disconnect() {
        connected = false;
        handler.disconnectFromServer();
    }

    // ==== RakNet thingy ====
    @Override
    public void connectionOpened(long serverId) {
        connected = true;
        
        LoginPacket pkLogin = new LoginPacket();
        pkLogin.clientID = client.getId();
        pkLogin.clientUuid = UUID.nameUUIDFromBytes(("DragonProxyPlayer:" + upstream.getUsername()).getBytes());
        pkLogin.protocol = Versioning.MINECRAFT_PE_PROTOCOL;
        pkLogin.serverAddress = "0.0.0.0:0";
        pkLogin.username = upstream.getUsername();
        pkLogin.skin = DefaultSkin.getDefaultSkin();
        
        System.out.println("[DEBUG] Remote pocket server downstream established! ");
        
        sendPacket(pkLogin, true);
    }

    @Override
    public void connectionClosed(String reason) {
        connected = false;
        upstream.disconnect(reason);
        System.out.println("[DEBUG] Remote pocket server downstream CLOSED! ");
    }

    @Override
    public void handleEncapsulated(EncapsulatedPacket packet, int flags) {
        byte[] buffer = Arrays.copyOfRange(packet.buffer, 1, packet.buffer.length);
        PEPacket pk = Protocol.decode(buffer);
        
        System.out.println("GOT PACKET = " + pk.getClass().getSimpleName());
        
        if(StartGamePacket.class.isAssignableFrom(pk.getClass())){
            // Translate
            StartGamePacket start = (StartGamePacket) pk;
            upstream.getDataCache().put(ENTITY_ID_KEY, (long) start.eid);
            
            SetSpawnPositionPacket spawn = new SetSpawnPositionPacket();
            spawn.x = start.spawnX;
            spawn.y = start.spawnY;
            spawn.z = start.spawnZ;
            upstream.sendPacket(spawn, true);
            
            MovePlayerPacket teleport = new MovePlayerPacket(0L, start.x, start.y, start.z, 0.0f, 0.0f, 0.0f, true);
            upstream.sendPacket(teleport, true);
            return;
        }
        upstream.sendPacket(pk);
    }

    @Override
    public void handleRaw(byte[] payload) {
    }

    @Override
    public void handleOption(String option, String value) {
    }

    public void sendPacket(PEPacket packet, boolean immediate) {
        if (packet == null) {
            return;
        }
        
        System.out.println("SENDING " + packet.getClass().getSimpleName());
        
        boolean overridedImmediate = immediate || packet.isShouldSendImmidate();
        packet.encode();
        if (packet.getData().length > 512 && !BatchPacket.class.isAssignableFrom(packet.getClass())) {
            BatchPacket pkBatch = new BatchPacket();
            pkBatch.packets.add(packet);
            sendPacket(pkBatch, overridedImmediate);
            return;
        }

        EncapsulatedPacket encapsulated = new EncapsulatedPacket();
        encapsulated.buffer = Binary.appendBytes((byte) 0xfe, packet.getData());
        encapsulated.needACK = true;
        encapsulated.reliability = (byte) 2;
        encapsulated.messageIndex = 0;
        handler.sendEncapsulated("", encapsulated, RakNet.FLAG_NEED_ACK | (overridedImmediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
    }

}
