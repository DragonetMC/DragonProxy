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
import java.util.Arrays;

import lombok.Getter;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.Binary;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.dragonet.raknet.server.RakNetServer;
import org.dragonet.raknet.server.ServerHandler;
import org.dragonet.raknet.server.ServerInstance;

import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.DataPacket;

public class RaknetInterface implements ServerInstance {

    @Getter
    private final DragonProxy proxy;

    private final SessionRegister sessions;

    @Getter
    private final RakNetServer rakServer;

    @Getter
    private final ServerHandler handler;

    public RaknetInterface(DragonProxy proxy, String ip, int port) {
        this.proxy = proxy;
        rakServer = new RakNetServer(port, ip);
        handler = new ServerHandler(rakServer, this);
        sessions = this.proxy.getSessionRegister();
    }

    public void setBroadcastName() {
        String name = "MCPE;";
        name += proxy.getMotd() + ";";
        name += Versioning.MINECRAFT_PE_PROTOCOL + ";";
        name += Versioning.MINECRAFT_PE_VERSION + ";";
        name += sessions.getOnlineCount() + ";" + proxy.getConfig().getMax_players();
        if (handler != null) {
            handler.sendOption("name", name);
        }
    }

    public void onTick() {
    	setBroadcastName();
        while (handler.handlePacket()) {
        }
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        UpstreamSession session = new UpstreamSession(proxy, identifier, new InetSocketAddress(address, port));
        sessions.newSession(session);
        setBroadcastName();
    }

    @Override
    public void closeSession(String identifier, String reason) {
        UpstreamSession session = sessions.getSession(identifier);
        if (session == null) {
            return;
        }
        session.onDisconnect(proxy.getLang().get(Lang.MESSAGE_CLIENT_DISCONNECT)); //It will handle rest of the things. 
        setBroadcastName();
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {
        UpstreamSession session = sessions.getSession(identifier);
        if (session == null) {
            return;
        }
        packet.buffer = Arrays.copyOfRange(packet.buffer, 1, packet.buffer.length);
        session.handlePacketBinary(packet);
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {
    }

    @Override
    public void handleOption(String option, String value) {
    }

    public void shutdown() {
        handler.shutdown();
    }

    public void disconnect(String identifier, String reason) {
        handler.closeSession(identifier, reason);
    }

    public void sendPacket(String identifier, DataPacket packet, boolean immediate) {
        if (identifier == null || packet == null) {
            return;
        }
        
        //Debug

        System.out.println("Sending [" + packet.getClass().getSimpleName() + "] after 2 seconds... ");
        if(packet.getClass().getSimpleName().contains("Chunk")){
        	System.out.print("");
        }
        try{
            //Thread.sleep(2000L);
        }catch(Exception e){}

        
        boolean overridedImmediate = immediate || false; /*packet.isShouldSendImmidate();*/
        packet.encode();
        if (packet.getBuffer().length > 512 && !BatchPacket.class.isAssignableFrom(packet.getClass())) {
            BatchPacket pkBatch = new BatchPacket();
            pkBatch.payload = packet.get();
            sendPacket(identifier, pkBatch, overridedImmediate);
            return;
        }


        EncapsulatedPacket encapsulated = new EncapsulatedPacket();
        encapsulated.buffer = Binary.appendBytes((byte) 0xfe, packet.getBuffer());
        encapsulated.needACK = true;
        encapsulated.reliability = (byte) 2;
        encapsulated.messageIndex = 0;
        handler.sendEncapsulated(identifier, encapsulated, RakNet.FLAG_NEED_ACK | (overridedImmediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
    }
}
