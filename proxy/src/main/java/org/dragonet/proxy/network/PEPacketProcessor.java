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

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;

import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.protocol.Protocol;
import org.dragonet.proxy.utilities.PatternChecker;
import org.spacehq.packetlib.packet.Packet;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.TextPacket;

public class PEPacketProcessor implements Runnable {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    @Getter
    private final UpstreamSession client;

    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(UpstreamSession client) {
        this.client = client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void run() {
        int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] bin = packets.pop();
            DataPacket[] packets = Protocol.decode(bin);
            if (packets == null || packets.length < 1) {
                continue;
            }
            
            for(DataPacket packet : packets){
            	handlePacket(packet);
            }
        }
    }

    public void handlePacket(DataPacket packet) {
        if (packet != null) {
            if(packet.pid() == ProtocolInfo.LOGIN_PACKET){
                try {
                	client.onLogin((LoginPacket) packet);
                } catch (Exception e){
                	e.printStackTrace();
                }
                return;
            }
        
	        if(packet.pid() == ProtocolInfo.TEXT_PACKET && client.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null){
	        	TextPacket pack = (TextPacket) packet;
	            if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")) {
	                if (!PatternChecker.matchEmail(pack.message.trim())) {
	                	
	                    client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    client.disconnect(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    return;
	                }
	                client.getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, pack.message.trim());
	                client.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
	                client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
	            } else if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")) {
	                if (client.getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || pack.message.equals(" ")) {
	                    client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    client.disconnect(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
	                    return;
	                }
	                client.sendChat(client.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
	                client.getDataCache().remove(CacheKey.AUTHENTICATION_STATE);
	                client.authenticateOnlineMode(pack.message); //We NEVER cache password for better security. 
	            }
	            return;
	        }
	        
	        Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
            if (translated != null && translated.length > 0 && client.getDownstream() != null && client.getDownstream().isConnected()) {
                client.getDownstream().send(translated);
            }
        }
    }
}
